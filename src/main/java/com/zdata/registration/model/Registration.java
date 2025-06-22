package com.zdata.registration.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Registration {
    private UUID studentId;
    private UUID courseId;
    private LocalDateTime registeredAt;

    public Registration(UUID studentId, UUID courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.registeredAt = LocalDateTime.now();
    }

    public UUID getStudentId() { return studentId; }
    public UUID getCourseId() { return courseId; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}