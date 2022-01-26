package com.example.demo.resetpassword;

import com.example.demo.resetpassword.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "api/reset-password")
public class ResetPasswordController {

    private final ResetPasswordService passwordService;

    @PostMapping(path = "/send-token/email/{email}")
    public void sendTokenToEmail(@Email(message = "Email invalid") @PathVariable("email") String email) {
        passwordService.sendTokenToEmail(email);
    }

    @PutMapping(path = "/reset/token/{token}/password/{password}")
    public void resetPassword(@PathVariable("token") String token,
                              @PathVariable("password")
                              @Size(min = 10,message = "Password should be at least 10 characters") String password) {
        passwordService.resetPassword(token, password);
    }
}
