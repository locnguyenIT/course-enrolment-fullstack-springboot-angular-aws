package com.example.demo.enrolment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment,Integer> {

    boolean existsByUserIdAndCourseId(int userId,int courseId);

    @Modifying
    @Query(value = "DELETE " +
                    "FROM enrolment " +
                    "WHERE user_id = ?1 AND course_id = ?2",nativeQuery = true)
    void deleteByUserIdAndCourseId(Integer userId, Integer courseId);

    List<Enrolment> findByUserId(Integer userId);
}
