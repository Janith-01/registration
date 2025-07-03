package com.zdata.registration.repository;

import com.zdata.registration.model.Course;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CourseRepository {

    private final Map<UUID, Course> courses = new HashMap<>();
    private final Map<String, UUID> courseCodeToId = new HashMap<>();

    public Course save(Course course) {
        courses.put(course.getId(), course);
        courseCodeToId.put(course.getCode(), course.getId());
        return course;
    }

    public boolean existsByCode(String code) {
        return courseCodeToId.containsKey(code);
    }

    public Optional<Course> findById(UUID id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Collection<Course> findAll() {
        return courses.values();
    }
}
