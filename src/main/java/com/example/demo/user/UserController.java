package com.example.demo.user;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.*;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public List<User> getListUser(){
        return  userService.getListUser();
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping(path = "/userId/{userId}")
    public User getUser(@PathVariable("userId") Integer userId ){
        return  userService.getUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/upload/image/userId/{userId}",consumes = MULTIPART_FORM_DATA_VALUE)
    public void uploadImageUserProfile (@PathVariable("userId") Integer userId,
                                        @RequestParam("file") MultipartFile file) {
        userService.uploadImageUserProfile(userId,file);
    }

    @GetMapping(path = "/show/{userId}/download/image",
                produces = {IMAGE_GIF_VALUE, IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    public byte[] downloadImage(@PathVariable("userId") Integer userId) {
        return userService.downloadImage(userId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add/roleId/{roleId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addUser(@PathVariable("roleId") Integer roleId,
                        @RequestParam("user") String user,
                        @RequestParam(value = "file",required = false)MultipartFile file){
        userService.addUser(roleId,user,file);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/userId/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId){
        userService.deleteUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping(path = "/update/userId/{userId}/userProfileId/{userProfileId}")
    public void updateUser(@PathVariable("userId") Integer userId,
                           @PathVariable("userProfileId") Integer userProfileId,
                           @RequestParam(value = "name",required = false) String name,
                           @Email(message = "Email invalid") @RequestParam(value = "email",required = false) String email,
                           @RequestParam(value = "address",required = false) String address,
                           @RequestParam(value = "enable",required = false) boolean enable,
                           @RequestParam(value = "roleId",required = false) Integer roleId,
                           @RequestParam(value = "file",required = false) MultipartFile file)
    {
         userService.updateUser(userId,userProfileId,name,email,address,enable,roleId,file);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @PutMapping(path = "/update/user-profile/userId/{userId}")
    public User updateUserProfile(@PathVariable("userId") Integer userId,
                           @RequestParam(value = "name",required = false) String name,
                           @Email(message = "Email invalid") @RequestParam(value = "email",required = false) String email,
                           @RequestParam(value = "address",required = false) String address,
                           @RequestParam(value = "enable",required = false) boolean enable,
                           @RequestParam(value = "roleId",required = false) Integer roleId)
    {
        return userService.updateUserProfile(userId,name,email,address,enable,roleId);
    }


}
