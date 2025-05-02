package com.gamba.software.slow_query_detector.db.repository;

import com.gamba.software.slow_query_detector.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find users by exact name match
    List<User> findByName(String name);
    
    // Find users by name containing the given string (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Find users by name starting with the given string (case-insensitive)
    List<User> findByNameStartingWithIgnoreCase(String name);
} 