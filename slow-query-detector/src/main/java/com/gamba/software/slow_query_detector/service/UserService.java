package com.gamba.software.slow_query_detector.service;

import com.gamba.software.slow_query_detector.db.UserDao;
import com.gamba.software.slow_query_detector.db.repository.UserRepository;
import com.gamba.software.slow_query_detector.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDao userDao;

    public UserService(UserRepository userRepository, UserDao userDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
    }

    public List<User> searchByExactName(String name) {
        //return userRepository.findByName(name);
        return userDao.searchByExactNameJdbc(name);
    }

    public List<User> searchByNameContains(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public List<User> searchByNameStartsWith(String name) {
        return userRepository.findByNameStartingWithIgnoreCase(name);
    }
} 