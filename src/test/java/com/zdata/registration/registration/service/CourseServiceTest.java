package com.zdata.registration.registration.service;

import com.zdata.registration.dto.CourseDto;
import com.zdata.registration.exception.ConflictException;
import com.zdata.registration.model.Course;
import com.zdata.registration.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    private CourseDto courseDto;

    @BeforeEach
    void setUp() {
        courseDto = new CourseDto();
        courseDto.setCode("CS101");
        courseDto.setTitle("Introduction to Programming");
        courseDto.setInstructor("Dr. Smith");
    }

    @Test
    void testAddCourse_Success() {
        Course course = courseService.addCourse(courseDto);
        assertNotNull(course);
        assertEquals(courseDto.getCode(), course.getCode());
        assertEquals(courseDto.getTitle(), course.getTitle());
        assertEquals(courseDto.getInstructor(), course.getInstructor());
    }

    @Test
    void testAddCourse_DuplicateCode() {
        courseService.addCourse(courseDto);
        assertThrows(ConflictException.class, () -> courseService.addCourse(courseDto));
    }
}