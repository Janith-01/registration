package com.zdata.registration.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegistrationDto {
    private UUID studentId;
    private UUID courseId;
    private LocalDateTime registeredAt;


    public UUID getStudentId() {
        return studentId;
    }
    public UUID getCourseId() {
        return courseId; }
    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }
    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }
    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt; }
}
