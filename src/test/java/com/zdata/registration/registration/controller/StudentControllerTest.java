package com.zdata.registration.registration.controller;

import com.zdata.registration.controller.StudentController;
import com.zdata.registration.dto.StudentDto;
import com.zdata.registration.model.Student;
import com.zdata.registration.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void testAddStudent_Success() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("John Doe");
        studentDto.setEmail("john.doe@example.com");

        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());

        when(studentService.addStudent(any(StudentDto.class))).thenReturn(student);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isCreated());
    }
}
