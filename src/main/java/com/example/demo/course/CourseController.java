package com.example.demo.course;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.*;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "api/course")
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public List<Course> getListCourse(){
        return courseService.getListCourse();
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping(path = "/category/categoryId/{categoryId}")
    public List<Course> getListCourseOfCategory(@PathVariable("categoryId") Integer categoryId){
        return courseService.getListCourseOfCategory(categoryId);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping(path = "/teacher/teacherId/{teacherId}")
    public List<Course> getListCourseOfTeacher(@PathVariable("teacherId") Integer teacherId){
        return courseService.getListCourseOfTeacher(teacherId);
    }

    @GetMapping(path = "/show/{courseId}/download/image",
            produces = {IMAGE_GIF_VALUE, IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
    public byte[] downloadImage(@PathVariable("courseId") Integer courseId) {
        return courseService.downloadImage(courseId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add/categoryId/{categoryId}/teacherId/{teacherId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addCourse(@PathVariable("categoryId") Integer categoryId,
                          @PathVariable("teacherId") Integer teacherId,
                          @RequestParam("course") String course,
                          @RequestParam(value = "file",required = false) MultipartFile file){
        courseService.addCourse(categoryId,teacherId,course,file);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/courseId/{courseId}")
    public void deleteCourse(@PathVariable("courseId") Integer courseId){
        courseService.deleteCourse(courseId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping(path = "update/courseId/{courseId}",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateCourse(@PathVariable("courseId") Integer courseId,
                             @RequestParam(value = "name",required = false) String name,
                             @RequestParam(value = "start_at",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime start_at,
                             @RequestParam(value = "end_at",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_at,
                             @RequestParam(value = "categoryId",required = false) Integer categoryId,
                             @RequestParam(value = "teacherId",required = false) Integer teacherId,
                             @RequestParam(value = "file",required = false) MultipartFile file){
        courseService.updateCourse(courseId,name,start_at,end_at,categoryId,teacherId,file);
    }
}
