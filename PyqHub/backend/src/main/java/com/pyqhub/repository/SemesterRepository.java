package com.pyqhub.repository;

import com.pyqhub.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findByCourseIdOrderByNumberAsc(Long courseId);
    Optional<Semester> findByCourseIdAndNumber(Long courseId, Integer number);
    boolean existsByCourseIdAndNumber(Long courseId, Integer number);
}
