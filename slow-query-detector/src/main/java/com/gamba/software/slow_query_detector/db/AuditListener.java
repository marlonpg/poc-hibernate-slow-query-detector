package com.gamba.software.slow_query_detector.db;

import org.springframework.stereotype.Component;

import jakarta.persistence.*;

@Component
public class AuditListener {

    @PrePersist @PreUpdate @PreRemove
    private void beforeAnyOperation(Object entity) {
        System.out.println("About to operate on: " + entity);
    }

    @PostLoad
    private void afterLoad(Object entity) {
        System.out.println("Loaded: " + entity);
    }
}