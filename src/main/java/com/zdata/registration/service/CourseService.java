package com.zdata.registration.service;

import com.zdata.registration.model.Course;
import com.zdata.registration.dto.request.CreateCourseRequest;
import com.zdata.registration.exception.DuplicateResourceException;
import com.zdata.registration.repository.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CreateCourseRequest request) {
        Optional<Course> existing = courseRepository.findByCode(request.getCode());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Course code already exists");
        }

        Course course = new Course();
        course.setId(UUID.randomUUID());
        course.setTitle(request.getName());
        course.setCode(request.getCode());

        courseRepository.save(course);

        return new CourseResponse(course.getId(), course.getTitle(), course.getCode());
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(c -> new CourseResponse(c.getId(), c.getTitle(), c.getCode()))
                .collect(Collectors.toList());
    }

    public Optional<Course> getCourseById(UUID id) {
        return courseRepository.findById(id);
    }
}
