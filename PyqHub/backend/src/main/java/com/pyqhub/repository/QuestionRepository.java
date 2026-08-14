package com.pyqhub.repository;

import com.pyqhub.entity.Question;
import com.pyqhub.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Advanced search across all hierarchy levels.
     * All parameters are optional — pass null to skip that filter.
     */
    @Query("""
        SELECT q FROM Question q
        JOIN q.topic t
        JOIN t.subject sub
        JOIN sub.semester sem
        JOIN sem.course c
        WHERE
            (:keyword IS NULL OR LOWER(q.questionText) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:year IS NULL OR q.year = :year)
            AND (:courseId IS NULL OR c.id = :courseId)
            AND (:courseName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :courseName, '%')))
            AND (:semesterNumber IS NULL OR sem.number = :semesterNumber)
            AND (:subjectId IS NULL OR sub.id = :subjectId)
            AND (:subjectName IS NULL OR LOWER(sub.name) LIKE LOWER(CONCAT('%', :subjectName, '%')))
            AND (:topicId IS NULL OR t.id = :topicId)
            AND (:topicName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :topicName, '%')))
            AND (:questionType IS NULL OR q.questionType = :questionType)
        """)
    Page<Question> searchQuestions(
            @Param("keyword") String keyword,
            @Param("year") Integer year,
            @Param("courseId") Long courseId,
            @Param("courseName") String courseName,
            @Param("semesterNumber") Integer semesterNumber,
            @Param("subjectId") Long subjectId,
            @Param("subjectName") String subjectName,
            @Param("topicId") Long topicId,
            @Param("topicName") String topicName,
            @Param("questionType") QuestionType questionType,
            Pageable pageable
    );

    Page<Question> findByAddedByCollegeId(String collegeId, Pageable pageable);
    Page<Question> findByTopicId(Long topicId, Pageable pageable);
    Page<Question> findByYear(Integer year, Pageable pageable);
}
