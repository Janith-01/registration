package com.zdata.registration.dto;

import jakarta.validation.constraints.NotBlank;

public class CourseDto {
    @NotBlank(message = "Code is mandatory")
    private String code;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Instructor is mandatory")
    private String instructor;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}