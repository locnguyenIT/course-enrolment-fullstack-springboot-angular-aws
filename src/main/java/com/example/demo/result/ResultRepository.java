package com.example.demo.result;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResultRepository extends JpaRepository<Result,Integer> {

    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    @Modifying
    @Query(value = "DELETE " +
                    "FROM result " +
                    "WHERE user_id = ?1 AND course_id = ?2",nativeQuery = true)
    void deleteByUserIdAndCourseId(Integer userId, Integer courseId);

    @Modifying
    @Query(value = "UPDATE result " +
                    "SET grade = ?1 " +
                    "WHERE user_id = ?2 AND course_id = ?3",nativeQuery = true)
    void updateGradeResultByUserIdAndCourseId(Integer grade,Integer userId, Integer courseId);
}
