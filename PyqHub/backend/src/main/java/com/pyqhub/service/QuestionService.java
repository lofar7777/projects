package com.pyqhub.service;

import com.pyqhub.dto.request.QuestionRequest;
import com.pyqhub.dto.response.QuestionResponse;
import com.pyqhub.entity.*;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.exception.UnauthorizedException;
import com.pyqhub.repository.QuestionRepository;
import com.pyqhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TopicService topicService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public Page<QuestionResponse> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable).map(this::toResponse);
    }

    public QuestionResponse getQuestionById(Long id) {
        return toResponse(questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id)));
    }

    /**
     * Full-text and filter-based search.
     * Every parameter is optional; null means "no filter on this field".
     */
    public Page<QuestionResponse> searchQuestions(
            String keyword,
            Integer year,
            Long courseId,
            String courseName,
            Integer semesterNumber,
            Long subjectId,
            String subjectName,
            Long topicId,
            String topicName,
            QuestionType questionType,
            Pageable pageable) {

        return questionRepository.searchQuestions(
                keyword, year, courseId, courseName,
                semesterNumber, subjectId, subjectName,
                topicId, topicName, questionType, pageable
        ).map(this::toResponse);
    }

    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request, String collegeId) {
        User user = userRepository.findByCollegeId(collegeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));
        Topic topic = topicService.getTopicById(request.getTopicId());

        Question question = Question.builder()
                .questionText(request.getQuestionText().trim())
                .year(request.getYear())
                .marks(request.getMarks())
                .questionType(request.getQuestionType() != null
                        ? request.getQuestionType() : QuestionType.SHORT_ANSWER)
                .explanation(request.getExplanation())
                .fileUrl(request.getFileUrl())
                .topic(topic)
                .addedBy(user)
                .build();

        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request, String collegeId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        User currentUser = userRepository.findByCollegeId(collegeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));

        // Only the original author or ADMIN can update
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = question.getAddedBy().getCollegeId().equals(collegeId);
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You can only edit your own questions");
        }

        Topic topic = topicService.getTopicById(request.getTopicId());

        // Handle file replacement
        if (request.getFileUrl() != null
                && !request.getFileUrl().equals(question.getFileUrl())
                && question.getFileUrl() != null) {
            fileStorageService.delete(question.getFileUrl());
        }

        question.setQuestionText(request.getQuestionText().trim());
        question.setYear(request.getYear());
        question.setMarks(request.getMarks());
        question.setQuestionType(request.getQuestionType() != null
                ? request.getQuestionType() : question.getQuestionType());
        question.setExplanation(request.getExplanation());
        question.setFileUrl(request.getFileUrl());
        question.setTopic(topic);

        return toResponse(questionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id, String collegeId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        User currentUser = userRepository.findByCollegeId(collegeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = question.getAddedBy().getCollegeId().equals(collegeId);
        if (!isAdmin && !isOwner) {
            throw new UnauthorizedException("You can only delete your own questions");
        }

        // Clean up attached file
        if (question.getFileUrl() != null) {
            fileStorageService.delete(question.getFileUrl());
        }
        questionRepository.deleteById(id);
    }

    public Page<QuestionResponse> getMyQuestions(String collegeId, Pageable pageable) {
        return questionRepository.findByAddedByCollegeId(collegeId, pageable).map(this::toResponse);
    }

    // ── Mapper ───────────────────────────────────────────────────────────────

    private QuestionResponse toResponse(Question q) {
        Topic topic = q.getTopic();
        Subject subject = topic.getSubject();
        Semester semester = subject.getSemester();
        Course course = semester.getCourse();

        return QuestionResponse.builder()
                .id(q.getId())
                .questionText(q.getQuestionText())
                .year(q.getYear())
                .marks(q.getMarks())
                .questionType(q.getQuestionType())
                .explanation(q.getExplanation())
                .fileUrl(q.getFileUrl())
                // Hierarchy breadcrumbs
                .topicId(topic.getId())
                .topicName(topic.getName())
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .semesterId(semester.getId())
                .semesterNumber(semester.getNumber())
                .courseId(course.getId())
                .courseName(course.getName())
                .courseCode(course.getCode())
                // Meta
                .addedByCollegeId(q.getAddedBy().getCollegeId())
                .addedByName(q.getAddedBy().getName())
                .createdAt(q.getCreatedAt())
                .updatedAt(q.getUpdatedAt())
                .build();
    }
}
