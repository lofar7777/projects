package com.pyqhub.service;

import com.pyqhub.dto.request.SemesterRequest;
import com.pyqhub.entity.Course;
import com.pyqhub.entity.Semester;
import com.pyqhub.exception.BadRequestException;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final CourseService courseService;

    public List<Semester> getSemestersByCourse(Long courseId) {
        courseService.getCourseById(courseId); // validates course exists
        return semesterRepository.findByCourseIdOrderByNumberAsc(courseId);
    }

    public Semester getSemesterById(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", id));
    }

    @Transactional
    public Semester createSemester(SemesterRequest request) {
        Course course = courseService.getCourseById(request.getCourseId());
        if (semesterRepository.existsByCourseIdAndNumber(request.getCourseId(), request.getNumber())) {
            throw new BadRequestException(
                    "Semester " + request.getNumber() + " already exists for this course");
        }
        Semester semester = Semester.builder()
                .number(request.getNumber())
                .course(course)
                .build();
        return semesterRepository.save(semester);
    }

    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Semester", id);
        }
        semesterRepository.deleteById(id);
    }
}
