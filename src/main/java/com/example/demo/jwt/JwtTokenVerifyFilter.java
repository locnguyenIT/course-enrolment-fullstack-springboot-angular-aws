package com.example.demo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifyFilter extends OncePerRequestFilter { //filter for once per request from client

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifyFilter(SecretKey secretKey,
                                JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. Get token from client on HttpHeader
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request,response); //rejected the request
            return;
        }
        //2. Verify the token
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(),"");
        try {

            //A signed JWT is called a 'JWS'.
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token);

            Claims body = claimsJws.getBody(); //payload

            String username = body.getSubject(); //subject

            List<Map<String,String>>  authorities = (List<Map<String,String>>)body.get("authorities"); //authority

            List<SimpleGrantedAuthority> grantedAuthority = authorities.stream().map(m ->
                    new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,grantedAuthority);

            SecurityContextHolder.getContext().setAuthentication(authentication); //JWT is valid will authentication for user

        }catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s can't be trust",token));
        }
        //3. Token is correct then this filter will send request & response to the next filter -> API
        filterChain.doFilter(request,response);
    }
}
