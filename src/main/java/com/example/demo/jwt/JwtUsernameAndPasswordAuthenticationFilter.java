package com.example.demo.jwt;

import com.example.demo.exception.NotFoundException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.*;


public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecretKey secretKey;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.secretKey = secretKey;
        setFilterProcessesUrl("/api/login");
    }

    //Authentication for user
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            //(api/login || POST) use ObjectMapper to read value from request and map to POJO UsernameAndPasswordAuthenticationRequest.class
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                                                    authenticationRequest.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authentication);

            return authenticate;

        } catch (IOException e) {
            throw  new IllegalStateException("Failed to authenticate user",e);
        }
    }

    //If successful authentication for user then create JWT and response JWT & userId to client
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        String secretKey = "secretKeysecretKeysecretKeysecretKey";
        String token = Jwts.builder()
                .setSubject(authResult.getName()) //subject (username)
                .claim("authorities",authResult.getAuthorities()) //claim
                .setIssuedAt(new Date()) //iat
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(5))) //// exp: expired 5 day
                .signWith(secretKey)
                .compact();
        //Send token & userId to client with ObjectMapper
        User user = userRepository.findByEmail(authResult.getName()).orElseThrow(()->
                new NotFoundException(String.format("User with '%s' was not found",authResult.getName())));
        Map<String,String> jwt = new HashMap<>();
        jwt.put("token",token);
        jwt.put("userId", String.valueOf(user.getId()));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),jwt);
    }

    //If unsuccessful authentication for user then send response exception UNAUTHORIZED 401 to client
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String,String> authFailed = new HashMap<>();
        authFailed.put("message",failed.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(),authFailed);
    }
}
