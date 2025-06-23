package com.zdata.registration.registration.service;

import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.model.Student;
import com.zdata.registration.service.CourseService;
import com.zdata.registration.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private StudentService studentService;

    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        studentDto = new StudentDto();
        studentDto.setName("John Doe");
        studentDto.setEmail("john.doe@example.com");

        // Use reflection to reset in-memory storage if needed
        try {
            java.lang.reflect.Field studentsField = StudentService.class.getDeclaredField("students");
            studentsField.setAccessible(true);
            studentsField.set(studentService, new HashMap<>());

            java.lang.reflect.Field emailToIdField = StudentService.class.getDeclaredField("emailToId");
            emailToIdField.setAccessible(true);
            emailToIdField.set(studentService, new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset in-memory storage", e);
        }
    }

    @Test
    void testAddStudent_Success() {
        // Act
        Student student = studentService.addStudent(studentDto);

        // Assert
        assertNotNull(student.getId());
        assertEquals("John Doe", student.getName());
        assertEquals("john.doe@example.com", student.getEmail());
    }

    @Test
    void testAddStudent_DuplicateEmail() {
        // Arrange
        studentService.addStudent(studentDto);


        ConflictException exception = assertThrows(ConflictException.class, () -> {
            studentService.addStudent(studentDto);
        });
        assertEquals("Email john.doe@example.com already exists", exception.getMessage());
    }
}