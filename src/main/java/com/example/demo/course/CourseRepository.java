package com.example.demo.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {

    Optional<Course> findByName(String name);

    List<Course> findByCategoryId(Integer categoryId);

    List<Course> findByTeacherId(Integer teacherId);
}
