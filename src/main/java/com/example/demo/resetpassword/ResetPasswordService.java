package com.example.demo.resetpassword;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mail.AWSMailService;
import com.example.demo.resetpassword.token.Token;
import com.example.demo.resetpassword.token.TokenRepository;
import com.example.demo.resetpassword.token.TokenService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ResetPasswordService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final AWSMailService AWSMailService;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void sendTokenToEmail(String email) {
        //1. Find the user
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new NotFoundException(String.format("User with email '%s' was not found",email)));
        //2. Create & Save token
        String token = UUID.randomUUID().toString();
        Token reset_password_token = new Token(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),user);
        tokenService.save(reset_password_token);
        //3. Send token to email
        String link = "http://courseenrollmentmanagementapplicaion-env.eba-tkyegmya.ap-southeast-1.elasticbeanstalk.com/#/reset-password";
        AWSMailService.send(user.getEmail(),buildEmail(user.getName(),link,token));
    }

    public String buildEmail(String name, String link, String token) {
        return  "<p>Hi "+ name +"</p>"
                + "<p>Thank you for visiting our website</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>This is your token: <strong>" +token+ "</strong></p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href=\"" + link + "\">Reset my password</a></p>"
                + "<p>Token will expire in 15 minutes</p>"
                + "<p>See you soon !</p>";
    }

    @Transactional
    public void resetPassword(String token, String password) {
        //1. Find token
        Token reset_password_token = tokenRepository.findByToken(token).orElseThrow(()->
                new NotFoundException("Token was not found"));
        //2. Get the user from token
        User user = userRepository.getById(reset_password_token.getUser().getId());
        //3. Check token already confirmed
        if (reset_password_token.getConfirmed_at() != null) {
            throw new BadRequestException("Email already confirmed");
        }
        //4. Check token is expired
        LocalDateTime expiredAt = reset_password_token.getExpired_at();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            tokenRepository.delete(reset_password_token);
            throw new BadRequestException("Token expired");
        }
        //5. Reset password
        user.setPassword(bCryptPasswordEncoder.encode(password));
        tokenRepository.updateConfirmedAt(token,LocalDateTime.now());
    }
}
