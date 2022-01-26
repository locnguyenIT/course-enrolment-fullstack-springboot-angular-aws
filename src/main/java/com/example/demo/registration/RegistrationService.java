package com.example.demo.registration;

import com.example.demo.aws.AWSFileStore;
import com.example.demo.exception.BadRequestException;
import com.example.demo.role.EnumRole;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.aws.AWSBucket.BUCKET_NAME;
import static com.example.demo.role.EnumRole.*;

@AllArgsConstructor
@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AWSFileStore awsFileStore;

    public void register(RegistrationRequest registrationRequest) {
        //1. Check the email
        Optional<User> userByEmail = userRepository.findByEmail(registrationRequest.getEmail());
        if (userByEmail.isPresent()) {
            throw new BadRequestException(String.format("User with email '%s' already used",registrationRequest.getEmail()));
        }
        //2. Check the name
        Optional<User> userByName = userRepository.findByName(registrationRequest.getName());
        if (userByName.isPresent()) {
            throw new BadRequestException(String.format("User with name '%s' already used",registrationRequest.getName()));
        }
        //3. Save
        Role role_user = roleRepository.findByName(USER);
        String passwordEncoder = bCryptPasswordEncoder.encode(registrationRequest.getPassword());
        User user = new User(registrationRequest.getName(), registrationRequest.getEmail(),
                            passwordEncoder,"user-default.png", LocalDateTime.now(),true,role_user);
        userRepository.save(user);
        //4.// Create folder for user
        String bucket = String.format("%s/user",BUCKET_NAME.getBucketName());
        String folder = String.format("userId-%d",user.getId());
        awsFileStore.create(bucket,folder);
        //copy from "user-default/user-default.png" and pass to folder "userId-%d/user-default.png" just created user
        String sourceKey = "user-default/user-default.png";
        String destinationKey = String.format("userId-%d/user-default.png",user.getId());
        awsFileStore.coppy(bucket,sourceKey,destinationKey);

    }
}
