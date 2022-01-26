package com.example.demo.enrolment;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.result.Result;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public List<Enrolment> getListEnrolment() {
        return enrolmentRepository.findAll();
    }

    public List<Enrolment> getListEnrolmentOfUser(Integer userId) {
        return enrolmentRepository.findByUserId(userId);
    }

    public void addEnrolment(Integer userId, Integer courseId, LocalDateTime create_at) {
        //1. Find the User
        User user = userRepository.findById(userId).orElseThrow(()->
                new NotFoundException("User was not found"));
        //2. Find the Course
        Course course = courseRepository.findById(courseId).orElseThrow(()->
                new NotFoundException("Course was not found"));
        //3. Check the enrolment exists
        boolean existsByUserIdAndCourseId = enrolmentRepository.existsByUserIdAndCourseId(user.getId(),course.getId());
        if(existsByUserIdAndCourseId){
           throw new BadRequestException(String.format("User '%s' already enrol '%s' course",user.getName(),course.getName()));
        }
        //4. Check if create_at of enrolment in (start_at & end_at) of course
        if(create_at.isBefore(course.getStart_at()) || create_at.isAfter(course.getEnd_at())){
            throw new BadRequestException("Can't enrol. Because your registration date is not within the allowable enrollment period of the course. " +
                                            "Please enrol for the next batch");
        }
        //5.Save Enrolment
        Enrolment enrolment = new Enrolment(new EnrolmentID(userId,courseId),user,course,create_at);
        enrolmentRepository.save(enrolment);
    }

    @Transactional
    public void deleteEnrolment(Integer userId, Integer courseId) {
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
        //3.Check the enrolment exists
        boolean existsByUserIdAndCourseId = enrolmentRepository.existsByUserIdAndCourseId(userId, courseId);
        if(!existsByUserIdAndCourseId){
            throw new NotFoundException(String.format("Enrolment of user id %d with course id %d was not found",userId,courseId));
        }
        //4. Delete
        enrolmentRepository.deleteByUserIdAndCourseId(userId,courseId);
    }


}
