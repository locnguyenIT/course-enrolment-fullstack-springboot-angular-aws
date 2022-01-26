package com.example.demo.registration;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public void register( @Valid @RequestBody RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
    }
}
