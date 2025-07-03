package com.zdata.registration.repository;

import com.zdata.registration.model.Registration;
import com.zdata.registration.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentRepository {

    private final Map<UUID, Student> students = new HashMap<>();
    private final Map<String, UUID> emailToId = new HashMap<>();
    private final Map<UUID, List<Registration>> studentRegistrations = new HashMap<>();

    public Student save(Student student) {
        students.put(student.getId(), student);
        emailToId.put(student.getEmail(), student.getId());
        studentRegistrations.put(student.getId(), new ArrayList<>());
        return student;
    }

    public Optional<Student> findById(UUID studentId) {
        return Optional.ofNullable(students.get(studentId));
    }

    public boolean existsByEmail(String email) {
        return emailToId.containsKey(email);
    }

    public UUID getIdByEmail(String email) {
        return emailToId.get(email);
    }

    public List<Registration> getRegistrations(UUID studentId) {
        return studentRegistrations.getOrDefault(studentId, new ArrayList<>());
    }

    public void addRegistration(UUID studentId, Registration registration) {
        studentRegistrations.get(studentId).add(registration);
    }

    public boolean removeRegistration(UUID studentId, UUID courseId) {
        return studentRegistrations.get(studentId).removeIf(r -> r.getCourseId().equals(courseId));
    }
}
