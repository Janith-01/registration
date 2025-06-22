package com.zdata.registration.repository;

import com.zdata.registration.model.Course;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CourseRepository {

    private final Map<UUID, Course> courses = new HashMap<>();

    public Course save(Course course) {
        courses.put(course.getId(), course);
        return course;
    }

    public Optional<Course> findById(UUID id) {
        return Optional.ofNullable(courses.get(id));
    }

    public Optional<Course> findByCode(String code) {
        return courses.values().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    public Collection<Course> findAll() {
        return courses.values();
    }

    public void deleteById(UUID id) {
        courses.remove(id);
    }

    public boolean existsByCode(String code) {
        return courses.values().stream()
                .anyMatch(c -> c.getCode().equalsIgnoreCase(code));
    }
}