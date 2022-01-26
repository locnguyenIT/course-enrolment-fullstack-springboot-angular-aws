package com.example.demo.resetpassword.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public void save(Token reset_password_token) {
        tokenRepository.save(reset_password_token);
    }
}
