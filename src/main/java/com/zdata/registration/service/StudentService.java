package com.zdata.registration.service;

import com.zdata.registration.dto.RegistrationDto;
import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.exception.ResourceNotFoundException;
import com.zdata.registration.model.Course;
import com.zdata.registration.model.Registration;
import com.zdata.registration.model.Student;
import com.zdata.registration.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<UUID, Student> students = new HashMap<>();
    private final Map<String, UUID> emailToId = new HashMap<>();
    private final Map<UUID, List<Registration>> studentRegistrations = new HashMap<>();
    private final CourseService courseService;

    public StudentService(CourseService courseService) {
        this.courseService = courseService;
    }

    public Student addStudent(StudentDto studentDto) {
        if (emailToId.containsKey(studentDto.getEmail())) {
            throw new ConflictException("Email " + studentDto.getEmail() + " already exists");
        }
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        students.put(student.getId(), student);
        emailToId.put(student.getEmail(), student.getId());
        studentRegistrations.put(student.getId(), new ArrayList<>());
        return student;
    }

    public Registration registerCourse(UUID studentId, UUID courseId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found");
        }
        courseService.getCourseById(courseId); // Validates course existence
        List<Registration> registrations = studentRegistrations.get(studentId);
        if (registrations.stream().anyMatch(r -> r.getCourseId().equals(courseId))) {
            throw new ConflictException("Student is already registered for this course");
        }
        Registration registration = new Registration(studentId, courseId);
        registrations.add(registration);
        return registration;
    }

    public void dropCourse(UUID studentId, UUID courseId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found");
        }
        courseService.getCourseById(courseId); // Validates course existence
        List<Registration> registrations = studentRegistrations.get(studentId);
        boolean removed = registrations.removeIf(r -> r.getCourseId().equals(courseId));
        if (!removed) {
            throw new ResourceNotFoundException("Student is not registered for this course");
        }
    }

    public List<Course> getRegisteredCourses(UUID studentId) {
        Student student = students.get(studentId);
        if (student == null) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " not found");
        }
        List<Registration> registrations = studentRegistrations.get(studentId);
        return registrations.stream()
                .map(r -> courseService.getCourseById(r.getCourseId()))
                .collect(Collectors.toList());
    }
}