package com.zdata.registration.controller;

import com.zdata.registration.dto.CourseDto;
import com.zdata.registration.model.Course;
import com.zdata.registration.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    //add a new course
    @PostMapping
    public ResponseEntity<Course> addCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.addCourse(courseDto);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    //get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    //get course by id
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        Course course = courseService.getCourseById(UUID.fromString(courseId));
        return ResponseEntity.ok(course);
    }
}
