package com.example.demo.result;


import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/result")
public class ResultController {

    private final ResultService resultService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping
    public List<Result> getListResult(){
        return resultService.getListResult();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add/userId/{userId}/courseId/{courseId}/grade/{grade}")
    public void addResult(@PathVariable("userId") Integer userId,
                          @PathVariable("courseId") Integer courseId,
                          @PathVariable("grade") Integer grade){
        resultService.addResult(userId,courseId,grade);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/userId/{userId}/courseId/{courseId}")
    public void deleteResult(@PathVariable("userId") Integer userId,
                                @PathVariable("courseId") Integer courseId){
        resultService.deleteResult(userId,courseId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping(path = "/update/userId/{userId}/courseId/{courseId}/grade/{grade}")
    public void updateResult(@PathVariable("userId") Integer userId,
                             @PathVariable("courseId") Integer courseId,
                             @PathVariable("grade") Integer grade){
        resultService.updateResult(userId,courseId,grade);
    }

}
