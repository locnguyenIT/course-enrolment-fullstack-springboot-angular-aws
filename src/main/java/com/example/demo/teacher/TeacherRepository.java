package com.example.demo.teacher;

import com.example.demo.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {


    Optional<Teacher> findByEmail(String email);
}
