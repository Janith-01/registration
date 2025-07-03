package com.zdata.registration.controller;

import com.zdata.registration.dto.RegistrationDto;
import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.model.Course;
import com.zdata.registration.model.Registration;
import com.zdata.registration.model.Student;
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

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Add a new student
    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody StudentDto studentDto) {
        Student student = studentService.addStudent(studentDto);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    // register a course for a student
    @PostMapping("/{studentId}/register/{courseId}")
    public ResponseEntity<Registration> registerCourse(@PathVariable UUID studentId, @PathVariable UUID courseId) {
        Registration registration = studentService.registerCourse(studentId, courseId);
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setStudentId(registration.getStudentId());
        registrationDto.setCourseId(registration.getCourseId());
        registrationDto.setRegisteredAt(registration.getRegisteredAt());
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    // Drop a course for a student
    @DeleteMapping("/{studentId}/drop/{courseId}")
    public ResponseEntity<Void> dropCourse(@PathVariable UUID studentId, @PathVariable UUID courseId) {
        studentService.dropCourse(studentId, courseId);
        return ResponseEntity.noContent().build();
    }

    //get all courses 
    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<Course>> getRegisteredCourses(@PathVariable UUID studentId) {
        List<Course> courses = studentService.getRegisteredCourses(studentId);
        return ResponseEntity.ok(courses);
    }
}