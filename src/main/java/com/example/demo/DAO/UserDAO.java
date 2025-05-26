package com.example.demo.DAO;

import com.example.demo.Model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    private final String GET_USER_BY_USERNAME = "SELECT * FROM user WHERE username =? ";
    private final String GET_USER_BY_USERID = "SELECT * FROM user WHERE userId =? ";
    private final String GET_ALL_USER = "SELECT * FROM user";
    private final String DELETE_USER = "DELETE FROM user WHERE userId = ?";
    private final String UPDATE_USER = "UPDATE user SET username = ?, phone = ?, gender = ?, email = ? WHERE userId = ?";
    private final String UPDATE_AVATAR = "UPDATE user SET avatar = ? WHERE userId = ?";

    public UserDAO() {}
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
    public void deleteUserById(String id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(DELETE_USER);
        ps.setString(1, id);
        ps.executeUpdate();
    }
    public ArrayList<User> getAll() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_ALL_USER);
        ArrayList<User> users = new ArrayList<>();
        ResultSet result = ps.executeQuery();

        if (result != null) {
            while (result.next()) {
                User user = new User();
                user.setUserId(result.getInt("userId"));
                user.setUsername(result.getString("userName"));
                user.setPassword(result.getString("password"));
                user.setGender(result.getString("gender"));
                user.setEmail(result.getString("email"));
                user.setAvatar(result.getString("avatar"));
                user.setRole(result.getString("role"));

                users.add(user);
            }
        }
        return users;
    }
    public User getUserByUsername(String name) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_USERNAME);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        if (rs.next()) {
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setGender(rs.getString("gender"));
            user.setEmail(rs.getString("email"));
            user.setAvatar(rs.getString("avatar"));
            user.setRole(rs.getString("role"));
        }
        return user;
    }
    public User getUserByUserId(int id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_USER_BY_USERID);
        ps.setString(1, String.valueOf(id));
        ResultSet rs = ps.executeQuery();
        User user = new User();
        if (rs.next()) {
            user.setUserId(rs.getInt("userId"));
            user.setUsername(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setGender(rs.getString("gender"));
            user.setEmail(rs.getString("email"));
            user.setAvatar(rs.getString("avatar"));
            user.setRole(rs.getString("role"));
        }
        return user;
    }
    public void updateUser(User user) throws SQLException {
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getGender());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getUserId());
            ps.executeUpdate();
        }
    }
    private static String CHANGE_PASSWORD="UPDATE user SET password =? WHERE userId=?";
    public void updatePassword(User user){
        try {
            Connection connection = getConnection();
            PreparedStatement ps =connection.prepareStatement(CHANGE_PASSWORD);
            ps.setString(1,user.getPassword());
            ps.setInt(2,user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkUsername(String name){
        try{
            Connection connection =getConnection();
            PreparedStatement ps =connection.prepareStatement(GET_USER_BY_USERNAME);
            ps.setString(1,name);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    private static final String CHECK_MAIL="SELECT * FROM user WHERE email=?";
    public boolean checkEmail(String mail){
        try{
            Connection connection =getConnection();
            PreparedStatement ps =connection.prepareStatement(CHECK_MAIL);
            ps.setString(1,mail);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    private static final String ADD_USER="INSERT INTO user(userName,email,password,gender,role) VALUES(?,?,?,?,'user')";
    public void addUser(User user){
        try{
            Connection connection=getConnection();
            PreparedStatement ps = connection.prepareStatement(ADD_USER);
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getPassword());
            ps.setString(4,user.getGender());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAvatar(int userId,String path){
        try{
            Connection connection = getConnection();
            PreparedStatement ps=connection.prepareStatement(UPDATE_AVATAR);
            ps.setString(1,path);
            ps.setInt(2,userId);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkExitEmail(String mail,int userId){
        try{
            Connection connection =getConnection();
            PreparedStatement ps =connection.prepareStatement(CHECK_MAIL);
            ps.setString(1,mail);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("userId");
                return userId == id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    public boolean checkExitUsername(String name,int userId){
        try{
            Connection connection= getConnection();
            PreparedStatement ps =connection.prepareStatement(GET_USER_BY_USERNAME);
            ps.setString(1,name);
            ResultSet rs= ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("userId");
                return userId == id;
            }
            else return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

