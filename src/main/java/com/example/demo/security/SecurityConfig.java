package com.example.demo.security;

import com.example.demo.auth.UserAuthDetailsService;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtTokenVerifyFilter;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthDetailsService userAuthDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Cross Origin Resource Sharing for Angular
                .cors()
                .and()
                //Cross Site Request Forgery protection
                .csrf().disable()
                //spring security not created session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //Handle Access Denied Exception Forbidden 403 if the role of User don't have enough permission to perform action
                .exceptionHandling().accessDeniedHandler(new AccessDeniedException())
                .and()
                //Authentication(username & password) from client & send token to client
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),userRepository, secretKey))
                //Verifier token from client
                .addFilterAfter(new JwtTokenVerifyFilter(secretKey, jwtConfig),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/","/favicon.ico","/*.js","/*.css","/api/login/**","/api/registration/**","/api/reset-password/**",
                                "/api/user/show/**","/api/teacher/show/**","/api/course/show/**").permitAll()
                .anyRequest()
                .authenticated();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userAuthDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }
}
