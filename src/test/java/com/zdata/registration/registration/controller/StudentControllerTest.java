package com.zdata.registration.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdata.registration.controller.StudentController;
import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.model.Student;
import com.zdata.registration.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddStudent_Success() throws Exception {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setName("John Doe");
        studentDto.setEmail("john.doe@example.com");

        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");

        when(studentService.addStudent(any(StudentDto.class))).thenReturn(student);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void testAddStudent_DuplicateEmail() throws Exception {
        // Arrange
        StudentDto studentDto = new StudentDto();
        studentDto.setName("John Doe");
        studentDto.setEmail("john.doe@example.com");

        when(studentService.addStudent(any(StudentDto.class)))
                .thenThrow(new com.zdata.registration.exception.ConflictException("Email john.doe@example.com already exists"));

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email john.doe@example.com already exists"));
    }
}