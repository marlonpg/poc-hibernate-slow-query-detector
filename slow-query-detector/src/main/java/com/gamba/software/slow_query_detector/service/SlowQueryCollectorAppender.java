package com.gamba.software.slow_query_detector.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.gamba.software.slow_query_detector.model.SlowQueryData;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;

public class SlowQueryCollectorAppender extends AppenderBase<ILoggingEvent> {

    // A thread-safe, bounded priority queue to store slow queries.
    // It's ordered by duration (longest first due to SlowQueryData's compareTo).
    private static final int MAX_STORED_QUERIES = 10; // Max number of unique slow queries to keep in memory
    private static final PriorityBlockingQueue<SlowQueryData> slowQueriesQueue =
            new PriorityBlockingQueue<>(MAX_STORED_QUERIES);

    private static final Pattern SLOW_QUERY_PATTERN = Pattern.compile(
            "Slow query took\\s*(\\d+)\\s*milliseconds\\s*\\[(.*)\\]$");
    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!"org.hibernate.SQL_SLOW".equals(eventObject.getLoggerName())) {
            return;
        }

        String message = eventObject.getFormattedMessage();
        Matcher matcher = SLOW_QUERY_PATTERN.matcher(message);

        if (matcher.find()) {
            try {
                long durationMs = Long.parseLong(matcher.group(1));
                String sql = matcher.group(2).trim();

                SlowQueryData queryData = new SlowQueryData(sql, durationMs);

                synchronized (slowQueriesQueue) {
                    if (slowQueriesQueue.size() < MAX_STORED_QUERIES) {
                        slowQueriesQueue.offer(queryData);
                    } else {

                        SlowQueryData fastestQueryInQueue = slowQueriesQueue.peek();
                        if (fastestQueryInQueue != null && queryData.getDurationMs() > fastestQueryInQueue.getDurationMs()) {
                            // The new query is slower than the "fastest of the slow queries" we are currently storing.
                            // So, remove the fastest one and add the new, slower one.
                            slowQueriesQueue.poll(); // Remove the head (fastest among the current top N)
                            slowQueriesQueue.offer(queryData); // Add the new query
                        }
                    }
                }

            } catch (NumberFormatException e) {
                addError("Could not parse slow query duration from message: " + message, e);
            } catch (Exception e) {
                addError("Error processing slow query log: " + message, e);
            }
        }
    }

    /**
     * Retrieves a snapshot of the collected slow queries.
     * The returned list is sorted by duration in DESCENDING order (slowest first).
     * @return A new list of SlowQueryData objects, sorted slowest first.
     */
    public static List<SlowQueryData> getSlowQueriesSnapshot() {
        List<SlowQueryData> snapshot;
        synchronized (slowQueriesQueue) {
            snapshot = new ArrayList<>(slowQueriesQueue);
        }
        snapshot.sort(Comparator.comparingLong(SlowQueryData::getDurationMs).reversed());
        return snapshot;
    }

    /**
     * Gets the top N slowest queries from the current snapshot.
     * @param n The number of top slow queries to retrieve.
     * @return A list of the top N SlowQueryData objects.
     */
    public static List<SlowQueryData> getTopNSlowestQueries(int n) {
        List<SlowQueryData> allSlowQueries = getSlowQueriesSnapshot();
        if (n <= 0) {
            return Collections.emptyList();
        }
        return allSlowQueries.stream().limit(n).collect(Collectors.toList());
    }

    /**
     * Clears all collected slow queries.
     */
    public static void clearSlowQueries() {
        synchronized (slowQueriesQueue) {
            slowQueriesQueue.clear();
        }
    }
}