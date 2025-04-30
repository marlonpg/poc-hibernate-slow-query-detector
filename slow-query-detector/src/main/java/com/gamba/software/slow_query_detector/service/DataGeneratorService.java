package com.gamba.software.slow_query_detector.service;

import com.gamba.software.slow_query_detector.model.User;
import com.gamba.software.slow_query_detector.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Service
public class DataGeneratorService {

    private static final Logger logger = Logger.getLogger(DataGeneratorService.class.getName());
    private final UserRepository userRepository;
    private final Random random = new Random();
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Jennifer", "Robert", "Lisa"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};

    @Autowired
    public DataGeneratorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void generateUsers(int count) {
        final int BATCH_SIZE = 1000;
        List<User> batch = new ArrayList<>(BATCH_SIZE);
        AtomicInteger counter = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        logger.info("Starting to generate " + count + " users...");

        for (int i = 0; i < count; i++) {
            User user = createRandomUser(counter.incrementAndGet());
            batch.add(user);

            if (batch.size() >= BATCH_SIZE) {
                userRepository.saveAll(batch);
                batch.clear();
                logger.info("Progress: " + (i + 1) + "/" + count + " users generated");
            }
        }

        if (!batch.isEmpty()) {
            userRepository.saveAll(batch);
        }

        long endTime = System.currentTimeMillis();
        logger.info("Generation completed in " + (endTime - startTime) / 1000 + " seconds");
    }

    private User createRandomUser(int id) {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String name = firstName + " " + lastName;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + id + "@example.com";

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
} 