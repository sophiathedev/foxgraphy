package com.example.demo.DAO;

import com.example.demo.Model.Follow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;

@Component
public class FollowDAO {
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;


    private static int cnt=0;
    private static final String GET_A_FOLLOW="SELECT * FROM follow WHERE userIdSrc=? AND userIdDst=?";
    private static final String DELETE_A_FOLLOW="DELETE FROM follow WHERE userIdSrc=? AND userIdDst=?";
    private static final String INSERT_A_FOLLOW="INSERT INTO follow(userIdSrc,userIdDst,time) VALUES(?,?,NOW())";
    private static final String COUNT_FOLLOW = "SELECT COUNT(*) FROM follow WHERE userIdDst=?";
    private static final String  GET_ALL_FOLLOWER="SELECT * FROM follow WHERE userIdDst=?";

    public FollowDAO(){}
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public int getStateFollow(int userIdSrc,int userIdDst) throws SQLException {
        Connection connection =getConnection();
        PreparedStatement ps = connection.prepareStatement(GET_A_FOLLOW);
        ps.setString(1, String.valueOf(userIdSrc));
        ps.setString(2, String.valueOf(userIdDst));
        ResultSet rs= ps.executeQuery();
        if(rs.next()){
            return 1;
        }
        else return 0;
    }
    public void delete(int userIdSrc,int userIdDst) throws SQLException {
        Connection connection=getConnection();
        PreparedStatement ps= connection.prepareStatement(DELETE_A_FOLLOW);
        ps.setString(1, String.valueOf(userIdSrc));
        ps.setString(2, String.valueOf(userIdDst));
        ps.execute();
    }
    public void insert(int userIdSrc,int userIdDst) throws SQLException {
        Connection connection =getConnection();
        PreparedStatement ps = connection.prepareStatement(INSERT_A_FOLLOW);
        ps.setString(1,String.valueOf(userIdSrc));
        ps.setString(2,String.valueOf(userIdDst));
        ps.execute();
    }
    public int countFollow(int userId){
        int ans=0;
        try{
            Connection connection=getConnection();
            PreparedStatement ps =connection.prepareStatement(COUNT_FOLLOW);
            ps.setString(1,String.valueOf(userId));
            ResultSet rs= ps.executeQuery();
            if(rs.next()){
                ans=rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
    public ArrayList<Integer> getAllFollower(int userId){
        ArrayList<Integer> follower=new ArrayList<>();
        try{
            Connection connection=getConnection();
            PreparedStatement ps =connection.prepareStatement(GET_ALL_FOLLOWER);
            ps.setInt(1,userId);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                follower.add(rs.getInt("userIdSrc"));
            }
            System.out.println(userId+" "+ follower.size());
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return follower;
    }
}
