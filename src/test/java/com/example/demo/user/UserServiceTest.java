package com.example.demo.user;

import com.example.demo.aws.AWSFileStore;
import com.example.demo.exception.NotFoundException;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.role.EnumRole.SUPER_ADMIN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //setup Mock UserRepository class;
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AWSFileStore awsFileStore;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    private UserService underTest ;
    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository,roleRepository,awsFileStore,passwordEncoder);
    }

    @Test
    void getListUser() {
        // When
        underTest.getListUser();
        // Then
        verify(userRepository).findAll();
    }

    @Test
    void itShouldGetUserByUserId() {


    }
    @Test
    @Disabled
    void canAddUser() throws JsonProcessingException {
        // Given
        Role role_super_admin = new Role(SUPER_ADMIN);

        given(roleRepository.findById(anyInt())).willReturn(Optional.of(role_super_admin));
        verify(roleRepository, Mockito.atLeastOnce()).save(any());

        User user = new User("Loc",
                "ntloc.developer@gmail.com",
                passwordEncoder.encode("password123456"),
                "ntloc.png","Khanh Hoa",
                LocalDateTime.now(),
                true,
                role_super_admin);

        MultipartFile file = new MockMultipartFile("file",
                                                "hello.jpg",
                                                MediaType.IMAGE_JPEG_VALUE,
                                                "Generate bytes to simulate a picture".getBytes());
        // When
        String userObject = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(user);
        underTest.addUser(role_super_admin.getId(),userObject,file);
        //Check user was given is right when userRepo.save(). Compare user was given == user when userRepo.save()
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User captureUser = userArgumentCaptor.getValue();

        assertThat(user).isEqualTo(captureUser);

    }

    @Test
    @Disabled
    void uploadImageUserProfile() {
    }

    @Test
    @Disabled
    void downloadImage() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void updateUserProfile() {
    }
}