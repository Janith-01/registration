package com.zdata.registration.controller;

import com.zdata.registration.dto.request.CreateStudentRequest;
import com.zdata.registration.dto.response.StudentResponse;
import com.zdata.registration.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final RegistrationService registrationService;

    public StudentController(StudentService studentService, RegistrationService registrationService) {
        this.studentService = studentService;
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{studentId}/register/{courseId}")
    public ResponseEntity<Void> registerToCourse(@PathVariable UUID studentId, @PathVariable UUID courseId) {
        registrationService.registerStudentToCourse(studentId, courseId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{studentId}/drop/{courseId}")
    public ResponseEntity<Void> dropCourse(@PathVariable UUID studentId, @PathVariable UUID courseId) {
        registrationService.dropStudentFromCourse(studentId, courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<CourseResponse>> getCourses(@PathVariable UUID studentId) {
        List<CourseResponse> courses = registrationService.getCoursesForStudent(studentId);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }
}
