package com.pyqhub.service;

import com.pyqhub.dto.request.CourseRequest;
import com.pyqhub.entity.Course;
import com.pyqhub.exception.BadRequestException;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    @Transactional
    public Course createCourse(CourseRequest request) {
        if (courseRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new BadRequestException("Course with code '" + request.getCode() + "' already exists");
        }
        Course course = Course.builder()
                .name(request.getName().trim())
                .code(request.getCode().toUpperCase())
                .description(request.getDescription())
                .build();
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, CourseRequest request) {
        Course course = getCourseById(id);
        course.setName(request.getName().trim());
        course.setCode(request.getCode().toUpperCase());
        course.setDescription(request.getDescription());
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", id);
        }
        courseRepository.deleteById(id);
    }
}
