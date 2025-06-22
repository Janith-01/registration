package com.zdata.registration.service;

import com.zdata.registration.model.Student;
import com.zdata.registration.dto.request.CreateStudentRequest;
import com.zdata.registration.dto.response.StudentResponse;
import com.zdata.registration.repository.StudentRepository;
import com.zdata.registration.exception.DuplicateResourceException;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponse createStudent(CreateStudentRequest request) {
        // Check for duplicate email
        Optional<Student> existing = studentRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Student with email already exists");
        }

        // Create and save new student
        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setName(request.getName());
        student.setEmail(request.getEmail());

        studentRepository.save(student);

        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentResponse(s.getId(), s.getName(), s.getEmail()))
                .collect(Collectors.toList());
    }

    public Optional<Student> getStudentById(UUID studentId) {
        return studentRepository.findById(studentId);
    }
}