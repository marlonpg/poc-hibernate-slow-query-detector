package com.gamba.software.slow_query_detector.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:testdb");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("password");
        hikariConfig.setDriverClassName("org.h2.Driver");

        DataSource originalDataSource = new HikariDataSource(hikariConfig);
        return originalDataSource;
    }
}
