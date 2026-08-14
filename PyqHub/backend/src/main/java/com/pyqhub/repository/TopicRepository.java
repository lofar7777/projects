package com.pyqhub.repository;

import com.pyqhub.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findBySubjectIdOrderByNameAsc(Long subjectId);
}
