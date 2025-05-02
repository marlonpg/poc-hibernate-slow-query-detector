package com.gamba.software.slow_query_detector.db;

import com.gamba.software.slow_query_detector.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {

    @Autowired
    private DataSource dataSource;

    public List<User> searchByExactNameJdbc(String name) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, email FROM app_user WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    users.add(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to search users by name", e);
        }

        return users;
    }

}
