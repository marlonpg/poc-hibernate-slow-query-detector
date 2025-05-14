package com.gamba.software.slow_query_detector.model;

import java.util.Objects;

public class SlowQueryData implements Comparable<SlowQueryData> {
    private final String sql;
    private final long durationMs;
    private final long timestamp;

    public SlowQueryData(String sql, long durationMs) {
        this.sql = sql;
        this.durationMs = durationMs;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSql() {
        return sql;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(SlowQueryData other) {
        return Long.compare(this.durationMs, other.durationMs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlowQueryData that = (SlowQueryData) o;
        return durationMs == that.durationMs &&
                timestamp == that.timestamp &&
                Objects.equals(sql, that.sql);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql, durationMs, timestamp);
    }

    @Override
    public String toString() {
        return "SlowQueryData{" +
                "durationMs=" + durationMs +
                "ms, sql='" + sql + '\'' +
                ", capturedAt=" + timestamp +
                '}';
    }
}