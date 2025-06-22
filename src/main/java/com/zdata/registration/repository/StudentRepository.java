package com.zdata.registration.repository;

import com.zdata.registration.model.Student;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentRepository {

    private final Map<UUID, Student> students = new HashMap<>();

    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public Optional<Student> findById(UUID id) {
        return Optional.ofNullable(students.get(id));
    }

    public Optional<Student> findByEmail(String email) {
        return students.values().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Collection<Student> findAll() {
        return students.values();
    }

    public void deleteById(UUID id) {
        students.remove(id);
    }

    public boolean existsByEmail(String email) {
        return students.values().stream()
                .anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }
}
