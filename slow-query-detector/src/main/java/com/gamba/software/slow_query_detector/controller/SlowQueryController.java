package com.gamba.software.slow_query_detector.controller;

import com.gamba.software.slow_query_detector.model.SlowQueryData;
import com.gamba.software.slow_query_detector.service.SlowQueryCollectorAppender;
import com.gamba.software.slow_query_detector.service.StatisticsHibernateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/slow-queries") // Choose an appropriate path
public class SlowQueryController {

    @Autowired
    private StatisticsHibernateService statisticsHibernateService;

    @GetMapping
    public List<SlowQueryData> getAllSlowQueries() {
        return SlowQueryCollectorAppender.getSlowQueriesSnapshot();
    }

    @GetMapping("/top/{n}")
    public List<SlowQueryData> getTopNSlowQueries(@PathVariable int n) {
        if (n <= 0) {
            // Or throw an IllegalArgumentException
            return List.of();
        }
        return SlowQueryCollectorAppender.getTopNSlowestQueries(n);
    }

    @GetMapping("/top3")
    public List<SlowQueryData> getTop3SlowQueries() {
        return statisticsHibernateService.getSlowQueriesTop(3);
    }

    @DeleteMapping("/clear")
    public String clearAllSlowQueries() {
        SlowQueryCollectorAppender.clearSlowQueries();
        return "Slow query log cleared.";
    }
}