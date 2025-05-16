package com.gamba.software.slow_query_detector.service;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class StatisticsHibernateService {
    private static final Logger logger = Logger.getLogger(StatisticsHibernateService.class.getName());

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Map<String, Long> getSlowQueriesTop(int num) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        Map<String, Long> map = new HashMap<>();

        for (String key : statistics.getSlowQueries().keySet()) {
            logger.info(key + statistics.getSlowQueries().get(key));
        }
        return map;
    }
}
