package com.gamba.software.slow_query_detector.service;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import java.util.logging.Logger;

public class TimingStatementInspector implements StatementInspector {
    private static final Logger logger = Logger.getLogger(TimingStatementInspector.class.getName());
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        // Capture the start time
        startTime.set(System.nanoTime());
        return sql;
    }

    // This method should be called after query execution
    public static void logExecutionTime(String sql) {
        Long start = startTime.get();
        if (start != null) {
            long duration = System.nanoTime() - start;
            logger.info(String.format("Query took %.2f ms: %s%n", duration / 1_000_000.0, sql));
            startTime.remove();
        }
    }
}