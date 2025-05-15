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

    @Autowired
    private SessionFactory sessionFactory;


    public Map<String, Long> getSlowQueriesTop(int num){
        SessionFactory sessionFactory1 = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics1 = sessionFactory1.getStatistics();
        Map<String, Long> map = new HashMap<>();

        Statistics statistics = sessionFactory.getStatistics();

        for (String key : statistics.getSlowQueries().keySet()){
            logger.info(key + statistics.getSlowQueries().get(key));
        }
        return map;
    }
}
