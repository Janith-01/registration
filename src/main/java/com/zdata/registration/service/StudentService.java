package com.zdata.registration.service;

import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.exception.ResourceNotFoundException;
import com.zdata.registration.model.Course;
import com.zdata.registration.model.Registration;
import com.zdata.registration.model.Student;
import com.zdata.registration.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final CourseService courseService;
    private final StudentRepository studentRepository;

    public StudentService(CourseService courseService, StudentRepository studentRepository) {
        this.courseService = courseService;
        this.studentRepository = studentRepository;
    }

    public Student addStudent(StudentDto studentDto) {
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new ConflictException("Email " + studentDto.getEmail() + " already exists");
        }

        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());

        return studentRepository.save(student);
    }

    public Registration registerCourse(UUID studentId, UUID courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " not found"));

        courseService.getCourseById(courseId); // Validate course

        List<Registration> registrations = studentRepository.getRegistrations(studentId);
        boolean alreadyRegistered = registrations.stream()
                .anyMatch(r -> r.getCourseId().equals(courseId));

        if (alreadyRegistered) {
            throw new ConflictException("Student is already registered for this course");
        }

        Registration registration = new Registration(studentId, courseId);
        studentRepository.addRegistration(studentId, registration);
        return registration;
    }

    public void dropCourse(UUID studentId, UUID courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " not found"));

        courseService.getCourseById(courseId);

        boolean removed = studentRepository.removeRegistration(studentId, courseId);
        if (!removed) {
            throw new ResourceNotFoundException("Student is not registered for this course");
        }
    }

    public List<Course> getRegisteredCourses(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + studentId + " not found"));

        List<Registration> registrations = studentRepository.getRegistrations(studentId);

        return registrations.stream()
                .map(r -> courseService.getCourseById(r.getCourseId()))
                .collect(Collectors.toList());
    }
}
