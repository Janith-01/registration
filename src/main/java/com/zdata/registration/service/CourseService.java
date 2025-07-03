package com.zdata.registration.service;

import com.zdata.registration.dto.CourseDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.exception.ResourceNotFoundException;
import com.zdata.registration.model.Course;
import com.zdata.registration.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course addCourse(CourseDto courseDto) {
        if (courseRepository.existsByCode(courseDto.getCode())) {
            throw new ConflictException("Course code " + courseDto.getCode() + " already exists");
        }

        Course course = new Course();
        course.setCode(courseDto.getCode());
        course.setTitle(courseDto.getTitle());
        course.setInstructor(courseDto.getInstructor());

        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll().stream().collect(Collectors.toList());
    }

    public Course getCourseById(UUID courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with ID " + courseId + " not found"));
    }
}
