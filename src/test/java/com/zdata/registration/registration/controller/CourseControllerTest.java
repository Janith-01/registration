package com.zdata.registration.registration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdata.registration.controller.CourseController;
import com.zdata.registration.dto.CourseDto;
import com.zdata.registration.model.Course;
import com.zdata.registration.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddCourse_Success() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCode("CS101");
        courseDto.setTitle("Introduction to Programming");
        courseDto.setInstructor("Dr. Smith");

        Course course = new Course();
        course.setId(UUID.randomUUID());
        course.setCode("CS101");
        course.setTitle("Introduction to Programming");
        course.setInstructor("Dr. Smith");

        when(courseService.addCourse(any(CourseDto.class))).thenReturn(course);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("CS101"))
                .andExpect(jsonPath("$.title").value("Introduction to Programming"))
                .andExpect(jsonPath("$.instructor").value("Dr. Smith"));

        verify(courseService, times(1)).addCourse(any(CourseDto.class));
    }

    @Test
    void testAddCourse_DuplicateCode() throws Exception {

        CourseDto courseDto = new CourseDto();
        courseDto.setCode("CS101");
        courseDto.setTitle("Introduction to Programming");
        courseDto.setInstructor("Dr. Smith");

        when(courseService.addCourse(any(CourseDto.class)))
                .thenThrow(new com.zdata.registration.exception.ConflictException("Course code CS101 already exists"));


        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Course code CS101 already exists"));

        verify(courseService, times(1)).addCourse(any(CourseDto.class));
    }

    @Test
    void testAddCourse_InvalidInput() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCode("");
        courseDto.setTitle("");
        courseDto.setInstructor("");


        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isBadRequest());

        verify(courseService, never()).addCourse(any(CourseDto.class));
    }

    @Test
    void testGetAllCourses_Success() throws Exception {

        Course course1 = new Course();
        course1.setId(UUID.randomUUID());
        course1.setCode("CS101");
        course1.setTitle("Introduction to Programming");
        course1.setInstructor("Dr. Smith");

        Course course2 = new Course();
        course2.setId(UUID.randomUUID());
        course2.setCode("CS102");
        course2.setTitle("Data Structures");
        course2.setInstructor("Dr. Jones");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        mockMvc.perform(get("/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("CS101"))
                .andExpect(jsonPath("$[0].title").value("Introduction to Programming"))
                .andExpect(jsonPath("$[1].code").value("CS102"))
                .andExpect(jsonPath("$[1].title").value("Data Structures"));

        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void testGetAllCourses_EmptyList() throws Exception {

        when(courseService.getAllCourses()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(courseService, times(1)).getAllCourses();
    }
}