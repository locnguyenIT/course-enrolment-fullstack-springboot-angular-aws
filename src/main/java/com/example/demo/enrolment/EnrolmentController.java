package com.example.demo.enrolment;

import com.example.demo.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/enrolment")
public class EnrolmentController {

    private final EnrolmentService enrolmentService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public List<Enrolment> getListEnrolment(){
        return enrolmentService.getListEnrolment();
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping(path = "/user/userId/{userId}")
    public List<Enrolment> getListEnrolmentOfUser(@PathVariable("userId") Integer userId){
        return enrolmentService.getListEnrolmentOfUser(userId);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add/userId/{userId}/courseId/{courseId}/create_at/{create_at}")
    public void addEnrolment(@PathVariable("userId") Integer userId,
                             @PathVariable("courseId") Integer courseId,
                             @PathVariable("create_at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime create_at){
        enrolmentService.addEnrolment(userId,courseId,create_at);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/userId/{userId}/courseId/{courseId}")
    public void deleteEnrolment(@PathVariable("userId") Integer userId,
                                @PathVariable("courseId") Integer courseId){
        enrolmentService.deleteEnrolment(userId,courseId);
    }

}
