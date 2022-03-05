package com.example.demo.teacher;

import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
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
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfTeacherPresentByEmail() {
        //Given
        String email = "hoang@gmail.com";
        Teacher teacher = new Teacher("Ho Viet Hoang",
                                    email,
                                    "teacher-1.jpg",
                                    "Software Engineer");
        underTest.save(teacher);
        // When
        Optional<Teacher> teacherByEmail = underTest.findByEmail(email);
        // Then
        assertThat(teacherByEmail).isPresent();
    }

    @Test
    void itShouldCheckIfTeacherNotPresentByEmail() {
        //Given
        String email = "ntloc.developer@gmail.com";
        // When
        Optional<Teacher> userByEmail = underTest.findByEmail(email);
        // Then
        assertThat(userByEmail).isNotPresent();
    }
}