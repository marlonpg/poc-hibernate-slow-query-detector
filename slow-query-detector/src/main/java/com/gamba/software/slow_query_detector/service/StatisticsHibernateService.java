package com.gamba.software.slow_query_detector.service;

import com.gamba.software.slow_query_detector.model.SlowQueryData;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class StatisticsHibernateService {
    private static final Logger logger = Logger.getLogger(StatisticsHibernateService.class.getName());

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List<SlowQueryData> getSlowQueriesTop(int num) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        Map<String, Long> map = new HashMap<>();
        List<SlowQueryData> slowQueryData = new ArrayList<>();
        for (String query : statistics.getSlowQueries().keySet()) {
            Long timeInMs = statistics.getSlowQueries().get(query);
            logger.info(query + " " + timeInMs);
            SlowQueryData queryData = new SlowQueryData(query, timeInMs);
            slowQueryData.add(queryData);
        }
        slowQueryData.sort(Comparator.reverseOrder());
        return slowQueryData.stream().limit(num).toList();
    }
}
