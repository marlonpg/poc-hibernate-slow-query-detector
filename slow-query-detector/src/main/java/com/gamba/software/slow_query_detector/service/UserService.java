package com.gamba.software.slow_query_detector.service;

import com.gamba.software.slow_query_detector.model.User;
import com.gamba.software.slow_query_detector.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchByExactName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> searchByNameContains(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public List<User> searchByNameStartsWith(String name) {
        return userRepository.findByNameStartingWithIgnoreCase(name);
    }
} 