package com.example.demo.teacher;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public List<Teacher> getListTeacher(){
        return teacherService.getListTeacher();
    }


    @GetMapping(path = "/show/{teacherId}/download/image",
                produces = {IMAGE_GIF_VALUE, IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    public byte[] downloadImage(@PathVariable("teacherId") Integer teacherId) {
        return teacherService.downloadImage(teacherId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public void addTeacher( @Valid @RequestParam("teacher") String teacher,
                            @RequestParam(value = "file",required = false) MultipartFile file) {
        teacherService.addTeacher(teacher,file);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/teacherId/{teacherId}")
    public void deleteTeacher(@PathVariable("teacherId") Integer teacherId){
        teacherService.deleteTeacher(teacherId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping(path = "update/teacherId/{teacherId}",
            consumes = MULTIPART_FORM_DATA_VALUE)
    public void updateTeacher(@PathVariable("teacherId") Integer teacherId,
                              @RequestParam(value = "name",required = false) String name,
                             @Email(message = "Email invalid") @RequestParam(value = "email",required = false) String email,
                             @RequestParam(value = "specialize",required = false) String specialize,
                             @RequestParam(value = "file",required = false) MultipartFile file){
       teacherService.updateTeacher(teacherId,name,email,specialize,file);
    }
}
