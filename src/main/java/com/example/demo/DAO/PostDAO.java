package com.example.demo.DAO;

import com.example.demo.Model.Post;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;


@Component
public class PostDAO {
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;
    private static final String ADD_A_POST = "INSERT INTO post(userId,title,tags,type,content,time,nameAuthor) VALUES(?,?,?,?,?,NOW(),?)";
    private static final String GET_POST_BY_ID= "SELECT * FROM post WHERE postId = ?";
    private static final String GET_ALL_POST= "SELECT * FROM post ORDER BY post.postId DESC";
    private static final String GET_ALL_POST_2= "SELECT * FROM post WHERE type ='post' ORDER BY post.postId DESC";
    private static final String GET_ALL_QUESTION= "SELECT * FROM post WHERE type ='question' ORDER BY post.postId DESC";
    private static final  String DELETE_POST = "DELETE FROM post WHERE postId = ?";
    private static final String GET_POSTS_BY_USER_ID = "SELECT * FROM post WHERE userId = ? AND type ='post' ORDER BY postId DESC";
    private static final String GET_QUESTIONS_BY_USER_ID = "SELECT * FROM post WHERE userId = ? AND type ='question' ORDER BY postId DESC";
    public void deletePostById (String id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = connection.prepareStatement(DELETE_POST);
        ps.setString(1 , id);
        ps.executeUpdate();
    }

    private static final String COUNT_POST_OF_USER = "SELECT count(*) FROM post WHERE userId=?";
    private static final String GET_ALL_POST_FOLLOW ="SELECT * FROM post JOIN follow ON post.userId = follow.userIdDst AND follow.userIdSrc=? AND post.type=? ORDER BY post.postId DESC";
    private static final String GET_ALL_POST_BOOKMARK ="SELECT * FROM post JOIN interaction ON post.postId = interaction.postId AND interaction.userId=?  AND post.type=? AND interaction.type='bookmark' ORDER BY post.postId DESC";
    private static final String SEARCH = "SELECT * FROM post WHERE lower(title) LIKE ? OR lower(tags) LIKE ? ORDER By postId DESC";
    private static final String UPDATE= "UPDATE post SET title = ?,tags=?,type=?,content=? WHERE postId=?";
    public PostDAO(){}
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

    public List<Post> getAllPost () {
        List<Post> posts = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_POST);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return posts;
    }
    public List<Post> getAllPost1(){
        List<Post> posts = new ArrayList<>();
        try {
            Connection connection =getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_POST_2);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
    public List<Post> getAllQuestion(){
        List<Post> posts = new ArrayList<>();
        try {
            Connection connection =getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_QUESTION);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
    public void addPost(Post post){
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(ADD_A_POST);

            ps.setString(1,String.valueOf(post.getUserId()));
            ps.setString(2,post.getTitle());
            ps.setString(3,post.getTags());
            ps.setString(4,post.getType());
            ps.setString(5,post.getContent());
            ps.setString(6,post.getNameAuthor());

            ps.execute();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Post getPostById(int id){
        Post post= new Post();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_POST_BY_ID);
            ps.setString(1,String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }
    public int countPost (int userId){
        int ans=0;
        try {
            Connection connection=getConnection();
            PreparedStatement ps= connection.prepareStatement(COUNT_POST_OF_USER);
            ps.setString(1,String.valueOf(userId));
            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                ans =rs.getInt("count(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
    public List<Post> getAllFollow(int userId,String type){
        List<Post> posts = new ArrayList<>();
        try{
            Connection connection=getConnection();
            PreparedStatement ps= connection.prepareStatement(GET_ALL_POST_FOLLOW);
            ps.setString(1,String.valueOf(userId));
            ps.setString(2,type);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
    public List<Post> getAllBookmark(int userId,String type){
        List<Post> posts = new ArrayList<>();
        try{
            Connection connection=getConnection();
            PreparedStatement ps= connection.prepareStatement(GET_ALL_POST_BOOKMARK);
            ps.setString(1,String.valueOf(userId));
            ps.setString(2,type);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
    public List<Post> search(String key){
        key= key.toLowerCase();
        key = "%"+key+"%";
        List<Post> ans = new ArrayList<>();
        try {
            Connection connection =getConnection();
            PreparedStatement ps =connection.prepareStatement(SEARCH);
            ps.setString(1,key);
            ps.setString(2,key);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                Post post =new Post();
                post.setPostId(rs.getInt("postId"));
                post.setType(rs.getString("type"));
                post.setTags(rs.getString("tags"));
                post.setContent(rs.getString("content"));
                post.setTitle(rs.getString("title"));
                post.setTime(Timestamp.valueOf(rs.getString("time")));
                post.setUserId(rs.getInt("userId"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                ans.add(post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
    public List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_POSTS_BY_USER_ID);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("postId"));
                post.setUserId(rs.getInt("userId"));
                post.setTitle(rs.getString("title"));
                post.setTags(rs.getString("tags"));
                post.setType(rs.getString("type"));
                post.setContent(rs.getString("content"));
                post.setTime(rs.getTimestamp("time"));
                post.setNameAuthor(rs.getString("nameAuthor"));
                posts.add(post);
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
    public List<Post> getQuestionsByUserId(int userId){
        List<Post> questions = new ArrayList<>();
        try {
            Connection connection= getConnection();
            PreparedStatement ps =connection.prepareStatement(GET_QUESTIONS_BY_USER_ID);
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Post question = new Post();
                question.setPostId(rs.getInt("postId"));
                question.setType(rs.getString("type"));
                question.setTags(rs.getString("tags"));
                question.setContent(rs.getString("content"));
                question.setTitle(rs.getString("title"));
                question.setTime(Timestamp.valueOf(rs.getString("time")));
                question.setUserId(rs.getInt("userId"));
                question.setNameAuthor(rs.getString("nameAuthor"));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }
    public boolean checkExit(int postId){
        try{
            Connection connection =getConnection();
            PreparedStatement ps= connection.prepareStatement(GET_POST_BY_ID);
            ps.setInt(1,postId);
            ResultSet rs= ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    public void update(Post post){
        try{
            Connection connection = getConnection();
            PreparedStatement ps =connection.prepareStatement(UPDATE);
            ps.setString(1,post.getTitle());
            ps.setString(2,post.getTags());
            ps.setString(3,post.getType());
            ps.setString(4,post.getContent());
            ps.setInt(5,post.getPostId());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
