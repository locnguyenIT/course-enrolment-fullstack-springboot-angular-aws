package com.example.demo.registration;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationRequest {

    @NotEmpty(message = "Name is empty")
    private String name;

    @Email(message = "Email invalid")
    private String email;

    @NotEmpty(message = "Password is empty")
    @Size(min = 10,message = "Password should be at least 10 characters")
    private String password;
}
