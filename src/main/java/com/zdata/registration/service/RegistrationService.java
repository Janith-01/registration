package com.zdata.registration.service;

import com.zdata.registration.model.Registration;
import com.zdata.registration.model.Student;
import com.zdata.registration.model.Course;
import com.zdata.registration.repository.RegistrationRepository;
import com.zdata.registration.repository.StudentRepository;
import com.zdata.registration.repository.CourseRepository;
import com.zdata.registration.exception.ResourceNotFoundException;
import com.zdata.registration.exception.DuplicateResourceException;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public void registerStudentToCourse(UUID studentId, UUID courseId) {
        // Validate student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Validate course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Check for duplicate registration
        Optional<Registration> existing = registrationRepository.findByStudentAndCourse(studentId, courseId);
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Student is already registered to this course");
        }

        Registration registration = new Registration(UUID.randomUUID(), studentId, courseId);
        registrationRepository.save(registration);
    }

    public void dropStudentFromCourse(UUID studentId, UUID courseId) {
        Registration registration = registrationRepository.findByStudentAndCourse(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));

        registrationRepository.delete(registration.getId());
    }

    public List<CourseResponse> getCoursesForStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<Registration> registrations = registrationRepository.findByStudentId(studentId);
        return registrations.stream()
                .map(reg -> {
                    Course course = courseRepository.findById(reg.getCourseId())
                            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
                    return new CourseResponse(course.getId(), course.getTitle(), course.getCode());
                })
                .collect(Collectors.toList());
    }
}