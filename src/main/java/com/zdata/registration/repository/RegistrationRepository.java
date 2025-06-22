package com.zdata.registration.repository;

import java.util.Optional;
import java.util.UUID;

import com.zdata.registration.model.Registration;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RegistrationRepository {

    private final Map<UUID, Registration> registrations = new HashMap<>();

    public Registration save(Registration registration) {
        registrations.put(registration.getId(), registration);
        return registration;
    }

    public Optional<Registration> findByStudentAndCourse(UUID studentId, UUID courseId) {
        return registrations.values().stream()
                .filter(r -> r.getStudentId().equals(studentId) && r.getCourseId().equals(courseId))
                .findFirst();
    }

    public List<Registration> findByStudentId(UUID studentId) {
        List<Registration> result = new ArrayList<>();
        for (Registration r : registrations.values()) {
            if (r.getStudentId().equals(studentId)) {
                result.add(r);
            }
        }
        return result;
    }

    public void delete(UUID id) {
        registrations.remove(id);
    }

    public Optional<Registration> findById(UUID id) {
        return Optional.ofNullable(registrations.get(id));
    }

    public Collection<Registration> findAll() {
        return registrations.values();
    }
}