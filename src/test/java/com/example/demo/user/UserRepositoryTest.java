package com.example.demo.user;

import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.role.EnumRole.SUPER_ADMIN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTestUser;
    @Autowired
    private RoleRepository underTestRole;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @AfterEach
    void tearDown() {
        underTestUser.deleteAll();
        underTestRole.deleteAll();
    }

    @Test
    void itShouldCheckIfUserPresentByEmail() {
        //Given
        Role role_super_admin = new Role(SUPER_ADMIN);
        underTestRole.save(role_super_admin);

        String email = "ntloc.developer@gmail.com";
        User user = new User("Loc",
                email,
                passwordEncoder.encode("password123456"),
                "ntloc.png","Khanh Hoa",
                LocalDateTime.now(),
                true,
                role_super_admin);
        underTestUser.save(user);
        // When
        Optional<User> userByEmail = underTestUser.findByEmail(email);
        // Then
        assertThat(userByEmail).isPresent();
    }

    @Test
    void itShouldCheckIfUserNotPresentByEmail() {
        //Given
        String email = "ntloc.developer@gmail.com";
        // When
        Optional<User> userByEmail = underTestUser.findByEmail(email);
        // Then
        assertThat(userByEmail).isNotPresent();
    }

    @Test
    void itShouldCheckIfUserPresentByName() {
        //Given
        Role role_super_admin = new Role(SUPER_ADMIN);
        underTestRole.save(role_super_admin);

        String name = "Loc";
        User user = new User(name,
                        "ntloc.developer@gmail.com",
                        passwordEncoder.encode("password123456"),
                        "ntloc.png","Khanh Hoa",
                        LocalDateTime.now(),
                        true,
                        role_super_admin);
        underTestUser.save(user);
        // When
        Optional<User> userByName = underTestUser.findByName(name);
        // Then
        assertThat(userByName).isPresent();
    }

    @Test
    void itShouldCheckIfUserNotPresentByName() {
        //Given
        String name = "Loc";
        // When
        Optional<User> userByName = underTestUser.findByName(name);
        // Then
        assertThat(userByName).isNotPresent();
    }


}