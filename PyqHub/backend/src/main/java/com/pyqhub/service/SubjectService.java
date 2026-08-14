package com.pyqhub.service;

import com.pyqhub.dto.request.SubjectRequest;
import com.pyqhub.entity.Semester;
import com.pyqhub.entity.Subject;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SemesterService semesterService;

    public List<Subject> getSubjectsBySemester(Long semesterId) {
        semesterService.getSemesterById(semesterId); // validates semester exists
        return subjectRepository.findBySemesterIdOrderByNameAsc(semesterId);
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", id));
    }

    @Transactional
    public Subject createSubject(SubjectRequest request) {
        Semester semester = semesterService.getSemesterById(request.getSemesterId());
        Subject subject = Subject.builder()
                .name(request.getName().trim())
                .code(request.getCode())
                .description(request.getDescription())
                .semester(semester)
                .build();
        return subjectRepository.save(subject);
    }

    @Transactional
    public Subject updateSubject(Long id, SubjectRequest request) {
        Subject subject = getSubjectById(id);
        Semester semester = semesterService.getSemesterById(request.getSemesterId());
        subject.setName(request.getName().trim());
        subject.setCode(request.getCode());
        subject.setDescription(request.getDescription());
        subject.setSemester(semester);
        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject", id);
        }
        subjectRepository.deleteById(id);
    }
}
