package com.example.demo.DAO;

import com.example.demo.Model.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationDAO {
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    private static final String SAVE_NOTICE = "INSERT INTO notifications (message, postId, userId, time) VALUES (?, ?, ?, NOW())";
    private static final String GET_ALL_NOTICE = "SELECT * FROM notifications WHERE userId = ? ORDER BY id DESC";
    private static final String UPDATE_STATE = "UPDATE notifications SET state = 0 WHERE state =1 AND userId=?";
    private static final String CHECK_EXIT_NEW_NOTIFICATION = "SELECT * FROM notifications WHERE userId=? AND state=1";

    public NotificationDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    public List<Notification> findByUserId(int userId) {
        List<Notification> notices = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_NOTICE);
            ps.setString(1, String.valueOf(userId));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification x = new Notification();
                x.setId(rs.getInt("id"));
                x.setMessage(rs.getString("message"));
                x.setPostId(rs.getInt("postId"));
                x.setUserId(rs.getInt("userId"));
                x.setTime(rs.getTimestamp("time"));
                x.setState(rs.getInt("state"));
                notices.add(x);
            }
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notices;
    }
    public void save(Notification notice) {
        try {
            System.out.println(notice.getMessage());
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(SAVE_NOTICE);
            ps.setString(1, notice.getMessage());
            ps.setInt(2, notice.getPostId());
            ps.setInt(3, notice.getUserId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(int userId) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATE_STATE);
            ps.setInt(1,userId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkExitNewNotifications(int userId) {
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(CHECK_EXIT_NEW_NOTIFICATION);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}