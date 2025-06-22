package com.zdata.registration.service;

import com.zdata.registration.dto.CourseDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.model.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CourseService {
    private final Map<UUID, Course> courses = new HashMap<>();
    private final Map<String, UUID> courseCodeToId = new HashMap<>();

    public Course addCourse(CourseDto courseDto) {
        if (courseCodeToId.containsKey(courseDto.getCode())) {
            throw new ConflictException("Course code " + courseDto.getCode() + " already exists");
        }
        Course course = new Course();
        course.setCode(courseDto.getCode());
        course.setTitle(courseDto.getTitle());
        course.setInstructor(courseDto.getInstructor());
        courses.put(course.getId(), course);
        courseCodeToId.put(course.getCode(), course.getId());
        return course;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Course getCourseById(UUID courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course with ID " + courseId + " not found");
        }
        return course;
    }
}