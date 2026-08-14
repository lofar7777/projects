package com.pyqhub.service;

import com.pyqhub.dto.request.TopicRequest;
import com.pyqhub.entity.Subject;
import com.pyqhub.entity.Topic;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final SubjectService subjectService;

    public List<Topic> getTopicsBySubject(Long subjectId) {
        subjectService.getSubjectById(subjectId); // validates subject exists
        return topicRepository.findBySubjectIdOrderByNameAsc(subjectId);
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", id));
    }

    @Transactional
    public Topic createTopic(TopicRequest request) {
        Subject subject = subjectService.getSubjectById(request.getSubjectId());
        Topic topic = Topic.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .subject(subject)
                .build();
        return topicRepository.save(topic);
    }

    @Transactional
    public Topic updateTopic(Long id, TopicRequest request) {
        Topic topic = getTopicById(id);
        Subject subject = subjectService.getSubjectById(request.getSubjectId());
        topic.setName(request.getName().trim());
        topic.setDescription(request.getDescription());
        topic.setSubject(subject);
        return topicRepository.save(topic);
    }

    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Topic", id);
        }
        topicRepository.deleteById(id);
    }
}
