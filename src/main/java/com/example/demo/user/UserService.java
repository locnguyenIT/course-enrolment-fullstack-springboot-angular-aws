package com.example.demo.user;

import com.example.demo.aws.AWSFileStore;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.role.Role;
import com.example.demo.role.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.aws.AWSBucket.*;
import static org.apache.http.entity.ContentType.*;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AWSFileStore awsFileStore;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User was not found"));
    }

    @Transactional
    public void uploadImageUserProfile(Integer userId, MultipartFile file) {
        //1. Find the user
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(("User was not found")));
        //2. Check the file
        if (file != null){
            //Check the ContentType of file is image or not
            if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                            IMAGE_GIF.getMimeType(),
                            IMAGE_PNG.getMimeType())
                    .contains(file.getContentType())){
                throw new IllegalStateException("File is not image");
            }
            //upload image to AWS S3
            Map<String,String> metadata = new HashMap<>();
            metadata.put("Content-Type",file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            String path = String.format("%s/user/userId-%d", BUCKET_NAME.getBucketName(),user.getId());
            String filename = String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
            try {
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            //Set imageURL
            user.setImageURL(filename);
        }

    }

    public byte[] downloadImage(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User was not found"));
        String path = String.format("%s/user/userId-%d", BUCKET_NAME.getBucketName(),user.getId());
        byte[] image = awsFileStore.download(path,user.getImageURL());

        return image;
    }

    public void addUser(Integer roleId, String user, MultipartFile file) {
        //1. Read value and mapping to POJO user with ObjectMapper from param user
        User userObject = new User();
        try {
            userObject = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(user, User.class);
            //2. Check if name of user belong to another user
            Optional<User> userByName = userRepository.findByName(userObject.getName());
            if (userByName.isPresent()){
                throw new BadRequestException(String.format("User with name '%s' already used",userObject.getName()));
            }
            //3. Check if email of user belong to another user
            Optional<User> userByEmail = userRepository.findByEmail(userObject.getEmail());
            if (userByEmail.isPresent()){
                throw new BadRequestException(String.format("User with email '%s' already used",userObject.getEmail()));
            }
            //4. Check the role
            Role role = roleRepository.findById(roleId).orElseThrow(()-> new NotFoundException("Role was not found"));

            userObject.setPassword(bCryptPasswordEncoder.encode(userObject.getPassword()));
            userObject.setRegister_at(LocalDateTime.now());
            userObject.setRole(role);
            //5. Check the file
            if (file != null){
                //Check the ContentType of file is image or not
                if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                                IMAGE_GIF.getMimeType(),
                                IMAGE_PNG.getMimeType())
                        .contains(file.getContentType())){
                    throw new IllegalStateException("File is not image");
                }
                //Set imageURL
                String filename = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());
                //6. Save
                userObject.setImageURL(filename);
                userRepository.save(userObject);
                //7. Upload image to AWS S3
                Map<String,String> metadata = new HashMap<>();
                metadata.put("Content-Type",file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));
                String path = String.format("%s/user/userId-%d", BUCKET_NAME.getBucketName(),userObject.getId());
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } else {
                //if file == null -> Set imageURL default & Save,
                userObject.setImageURL("user-default.png");
                userRepository.save(userObject);
                //create new folder for user
                String bucket = String.format("%s/user",BUCKET_NAME.getBucketName());
                String folder = String.format("userId-%d",userObject.getId());
                awsFileStore.create(bucket,folder);
                //copy from "user-default/user-default.png" and pass to folder "userId-%d/user-default.png" just created user
                String sourceKey = "user-default/user-default.png";
                String destinationKey = String.format("userId-%d/user-default.png",userObject.getId());
                awsFileStore.coppy(bucket,sourceKey,destinationKey);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteUser(Integer userId) {
        //1. Check the Course exists
        boolean existsUser = userRepository.existsById(userId);
        if (!existsUser){
            throw new NotFoundException("User was not found");
        }
        //2. Delete
//        String bucket = String.format("%s", BUCKET_NAME.getBucketName());
//        String key = String.format("user/userId-%d",userId);
//        awsFileStore.delete(bucket,key);
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Integer userId, Integer userProfileId, String name, String email, String address,
                           boolean enable, Integer roleId, MultipartFile file) {

        //1. Find userProfile
        User userProfile = userRepository.findById(userProfileId).orElseThrow(() -> new NotFoundException("User was not found"));
        //2. Find the user
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("This user was not found"));
        // User with role is ADMIN can't update user role is ADMIN & SUPER_ADMIN. Just update user role is USER
        if (userProfile.getRole().getName().name().equals("ADMIN")) {
            if (user.getRole().getName().name().equals("ADMIN")) {
                throw new BadRequestException("You can't update users with role is ADMIN, you can only update users have role is USER");
            }
            if (user.getRole().getName().name().equals("SUPER_ADMIN")) {
                throw new BadRequestException("You can't update users with role is SUPER_ADMIN, you can only update users have role is USER");
            }
        }
        // Update name
        if (name != null && name.length() > 0 && !Objects.equals(user.getName(),name)){
            //Check if name of user belong to another user
            Optional<User> userByName = userRepository.findByName(name);
            if (userByName.isPresent()){
                throw new BadRequestException(String.format("User with name '%s' already used",name));
            }
            user.setName(name);
        }
        //Update email
        if (email != null && !Objects.equals(user.getEmail(),email)){
            //Check if email of user belong to another user
            Optional<User> userByEmail = userRepository.findByEmail(email);
            if (userByEmail.isPresent()){
                throw new BadRequestException(String.format("User with email '%s' already used",email));
            }
            user.setEmail(email);
        }
        //Update Address
        if (address != null && !Objects.equals(user.getAddress(),address)){
            user.setAddress(address);
        }
        // Update enable
        user.setEnable(enable);
        // Update Role
        if (roleId != null && !Objects.equals(user.getRole(),roleId)){
            //Check the role exists
            Role role = roleRepository.findById(roleId).orElseThrow(()-> new NotFoundException("The role was not found"));
            user.setRole(role);
        }
        // Update Image
        if (file != null){
            //Check the ContentType of file is image or not
            if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                            IMAGE_GIF.getMimeType(),
                            IMAGE_PNG.getMimeType())
                    .contains(file.getContentType())){
                throw new IllegalStateException("File is not image");
            }
            //upload image to AWS S3
            Map<String,String> metadata = new HashMap<>();
            metadata.put("Content-Type",file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));
            String path = String.format("%s/user/userId-%d", BUCKET_NAME.getBucketName(),user.getId());
            String filename = String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
            try {
                awsFileStore.upload(path,filename,file.getInputStream(), Optional.of(metadata));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            //Set imageURL
            user.setImageURL(filename);
        }
    }

    @Transactional
    public User updateUserProfile(Integer userId, String name, String email,
                                  String address, boolean enable, Integer roleId) {

        //1. Find the user
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundException("This user was not found"));
        //2. Update name
        if (name != null && name.length() > 0 && !Objects.equals(user.getName(),name)){
            //Check if name of user belong to another user
            Optional<User> userByName = userRepository.findByName(name);
            if (userByName.isPresent()){
                throw new BadRequestException(String.format("User with name '%s' already used",name));
            }
            user.setName(name);
        }
        //3. Update email
        if (email != null && !Objects.equals(user.getEmail(),email)){
            //Check if email of user belong to another user
            Optional<User> userByEmail = userRepository.findByEmail(email);
            if (userByEmail.isPresent()){
                throw new BadRequestException(String.format("User with email '%s' already used",email));
            }
            user.setEmail(email);
        }
        //4. Update Address
        if (address != null && !Objects.equals(user.getAddress(),address)){
            user.setAddress(address);
        }
        //5. Update enable
        user.setEnable(enable);
        //6. Update Role
        if (roleId != null && !Objects.equals(user.getRole(),roleId)){
            //Check the role exists
            Role role = roleRepository.findById(roleId).orElseThrow(()-> new NotFoundException("The role was not found"));
            user.setRole(role);
        }

        return user;
    }


}
