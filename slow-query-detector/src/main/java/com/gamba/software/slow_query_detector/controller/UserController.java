package com.gamba.software.slow_query_detector.controller;

import com.gamba.software.slow_query_detector.model.User;
import com.gamba.software.slow_query_detector.service.DataGeneratorService;
import com.gamba.software.slow_query_detector.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DataGeneratorService dataGeneratorService;

    public UserController(UserService userService, DataGeneratorService dataGeneratorService) {
        this.userService = userService;
        this.dataGeneratorService = dataGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateUsers(@RequestParam(defaultValue = "1000000") int count) {
        dataGeneratorService.generateUsers(count);
        return ResponseEntity.ok("Generated " + count + " users successfully");
    }

    @GetMapping("/search/exact")
    public ResponseEntity<List<User>> searchByExactName(@RequestParam String name) {
        List<User> users = userService.searchByExactName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/contains")
    public ResponseEntity<List<User>> searchByNameContains(@RequestParam String name) {
        List<User> users = userService.searchByNameContains(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/starts-with")
    public ResponseEntity<List<User>> searchByNameStartsWith(@RequestParam String name) {
        List<User> users = userService.searchByNameStartsWith(name);
        return ResponseEntity.ok(users);
    }
} 