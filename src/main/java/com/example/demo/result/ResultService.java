package com.example.demo.result;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.enrolment.EnrolmentID;
import com.example.demo.enrolment.EnrolmentRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrolmentRepository enrolmentRepository;

    public List<Result> getListResult() {
        return resultRepository.findAll();
    }

    public void addResult(Integer userId, Integer courseId, Integer grade) {
        //1. Find the student
        User user = userRepository.findById(userId).orElseThrow(()->
                new NotFoundException(String.format("User with id %d was not found",userId)));
        //2.Find the course
        Course course = courseRepository.findById(courseId).orElseThrow(()->
                new NotFoundException(String.format("Course with id %d was not found",courseId)));
        //3. Check if the user has enrolment for that course
        boolean existsEnrolment =  enrolmentRepository.existsByUserIdAndCourseId(user.getId(),course.getId());
        if(!existsEnrolment){
            throw new BadRequestException(String.format("User '%s' is not registered '%s' course ",user.getName(),course.getName()));
        }
        //4. Check if the result exists
        boolean existsByUserIdAndCourseId = resultRepository.existsByUserIdAndCourseId(user.getId(), course.getId());
        if(existsByUserIdAndCourseId){
            throw new BadRequestException(String.format("User '%s' already has the grade of '%s' course ",user.getName(),course.getName()));
        }
        //5. Save
        Result result = new Result(new ResultID(userId,courseId),user,course,grade);
        resultRepository.save(result);
    }

    @Transactional
    public void deleteResult(Integer userId, Integer courseId) {
        //1. Check the student exists
        boolean userExists = userRepository.existsById(userId);
        if(!userExists){
            throw new NotFoundException(String.format("User with id %d was not found",userId));
        }
        //2. Check the course exists
        boolean courseExists = courseRepository.existsById(courseId);
        if(!courseExists){
            throw new NotFoundException(String.format("Course with id %d was not found",courseId));
        }
        //3. Check if the result exists
        boolean existsByUserIdAndCourseId = resultRepository.existsByUserIdAndCourseId(userId, courseId);
        if(!existsByUserIdAndCourseId){
            throw new NotFoundException(String.format("Result of user id %d with course id %d was not found",userId,courseId));
        }
        //4. Delete
        resultRepository.deleteByUserIdAndCourseId(userId,courseId);
    }

    @Transactional
    public void updateResult(Integer userId, Integer courseId, Integer grade) {
        //1. Check the student exists
        boolean userExists = userRepository.existsById(userId);
        if(!userExists){
            throw new NotFoundException(String.format("User with id %d was not found",userId));
        }
        //2. Check the course exists
        boolean courseExists = courseRepository.existsById(courseId);
        if(!courseExists){
            throw new NotFoundException(String.format("Course with id %d was not found",courseId));
        }
        //3. Check if the result exists
        boolean existsByUserIdAndCourseId = resultRepository.existsByUserIdAndCourseId(userId, courseId);
        if(!existsByUserIdAndCourseId){
            throw new NotFoundException(String.format("Result of user id %d with course id %d was not found",userId,courseId));
        }
        //4. Update grade
        if(grade != null){
            resultRepository.updateGradeResultByUserIdAndCourseId(grade,userId,courseId);
        }
    }
}
