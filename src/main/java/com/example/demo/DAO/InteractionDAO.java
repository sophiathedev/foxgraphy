package com.example.demo.DAO;

import com.example.demo.Model.Interaction;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

@Component
public class InteractionDAO {
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String jdbcUsername;
    @Value("${spring.datasource.password}")
    private String jdbcPassword;


    private static final String COUNT_VOTE_UP="SELECT count(DISTINCT interactionId) FROM interaction WHERE postId= ? AND type ='up'";
    private static final String COUNT_VOTE_DOWN ="SELECT count(DISTINCT interactionId) FROM interaction WHERE postId= ? AND type ='down'";
    private static final String GET_INTERACTION="SELECT * FROM interaction WHERE postId=? AND userId= ? AND (type =? OR type =?)";
    private static final String DELETE_INTERACTION ="DELETE FROM interaction WHERE interactionId= ?";
    private static final String UPDATE_VOTE = "UPDATE interaction SET time = NOW() ,type =? WHERE interactionId=?";
    private static final String ADD_INTERACTION = "INSERT INTO interaction (userId,commentId,postId,type,time) VALUES (?,-1,?,?,NOW())";
    private static final String COUNT_BOOKMARK ="SELECT count(*) FROM interaction WHERE postId=? AND type='bookmark'";

    public InteractionDAO(){}
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
    // tương tác vote bài viết
    public int getNumVote(String id) throws SQLException {
        Connection connection = getConnection();

        PreparedStatement voteUp = connection.prepareStatement(COUNT_VOTE_UP);
        voteUp.setString(1,String.valueOf(id));
        ResultSet rs=voteUp.executeQuery();
        int cntVoteUp=0,cntVoteDown=0;
        if(rs.next()) {
            cntVoteUp = rs.getInt("count(DISTINCT interactionId)");
        }
        PreparedStatement voteDown = connection.prepareStatement(COUNT_VOTE_DOWN);
        voteDown.setString(1,String.valueOf(id));
        ResultSet rs1= voteDown.executeQuery();
        if(rs1.next()){
            cntVoteDown= rs1.getInt("count(DISTINCT interactionId)");
        }
        return cntVoteUp - cntVoteDown;
    }
    private Interaction getInteraction(int postId, int userId,String type1,String type2) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps= connection.prepareStatement(GET_INTERACTION);
        ps.setString(1,String.valueOf(postId));
        ps.setString(2,String.valueOf(userId));
        ps.setString(3,type1);
        ps.setString(4,type2);
        ResultSet rs = ps.executeQuery();

        Interaction vote =new Interaction(); // chưa vote

        if(rs.next()){
            vote.setInteractionId(rs.getInt("interactionId"));
            vote.setUserId(rs.getInt("userId"));
            vote.setPostId(rs.getInt("postId"));
            vote.setType(rs.getString("type"));
            vote.setTime(rs.getTimestamp("time"));
        }
        return vote;
    }
    public int getStateVote(int postId, int userId) throws SQLException {
        Interaction vote = getInteraction(postId,userId,"up","down");
        if(vote.getType() == null) return 0;
        int userVote=0;
        if(vote.getType().equals("up")){
            userVote=1;
        }
        else userVote=-1;
        return userVote;
    }
    public void addInteractionPost(int postId, int userId, String type) throws SQLException {

        if(type.equals("bookmark")){
            Interaction bookmark = getInteraction(postId,userId,"bookmark","bookmark");
            Connection connection =getConnection();
            if(bookmark.getType()== null){
                PreparedStatement addBookmark= connection.prepareStatement(ADD_INTERACTION);

                addBookmark.setString(1,String.valueOf(userId));
                addBookmark.setString(2,String.valueOf(postId));
                addBookmark.setString(3,type);
                addBookmark.execute();
            }
            else{
                PreparedStatement deleteBookmark=connection.prepareStatement(DELETE_INTERACTION);
                deleteBookmark.setString(1, String.valueOf(bookmark.getInteractionId()));
                deleteBookmark.execute();
            }
        }
        else{
            Interaction vote= getInteraction(postId,userId,"up","down");
            Connection connection =getConnection();
            if(vote.getType()== null){
                PreparedStatement addVote = connection.prepareStatement(ADD_INTERACTION);

                addVote.setString(1,String.valueOf(userId));
                addVote.setString(2,String.valueOf(postId));
                addVote.setString(3,type);
                addVote.execute();
            }
            else {
                if(type.equals(vote.getType())) {
                    PreparedStatement delete = connection.prepareStatement(DELETE_INTERACTION);
                    delete.setString(1, String.valueOf(vote.getInteractionId()));
                    delete.execute();
                }
                else {
                    PreparedStatement update = connection.prepareStatement(UPDATE_VOTE);
                    update.setString(1,type);
                    update.setString(2,String.valueOf(vote.getInteractionId()));
                    update.execute();
                }
            }
        }
    }
    // tương tác bookmark bài viết
    public int getStateBookmark(int postId,int userId) throws SQLException {
        Interaction bookmark = getInteraction(postId,userId,"bookmark","bookmark");
        if(bookmark.getType() == null) return 0;
        else return 1;
    }
    public int countBookmark(int postId){
        int ans=0;
        try{
            Connection connection =getConnection();
            PreparedStatement ps= connection.prepareStatement(COUNT_BOOKMARK);
            ps.setString(1,String.valueOf(postId));
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                ans = rs.getInt("count(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
    // đém số lượt xem bài viết
    private static final String COUNT_VIEW = "SELECT COUNT(DISTINCT interactionId) FROM interaction WHERE postId=? AND type ='view'";
    public int countView(int postId){
        int ans=0;
        try {
            Connection connection= getConnection();
            PreparedStatement ps =connection.prepareStatement(COUNT_VIEW);
            ps.setString(1,String.valueOf(postId));
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
                ans = rs.getInt("COUNT(DISTINCT interactionId)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }

    // tương tác với comment
    private static final String COUNT_VOTE_DOWN_COMMENT="SELECT count(DISTINCT interactionId) FROM interaction WHERE commentId= ? AND type ='down'";
    private static final String COUNT_VOTE_UP_COMMENT="SELECT count(DISTINCT interactionId) FROM interaction WHERE commentId= ? AND type ='up'";
    private static final String GET_INTERACTION_COMMENT="SELECT * FROM interaction WHERE commentId=? AND userId= ? AND (type ='down' OR type ='up')";
    private static final String DELETE_INTERACTION_COMMENT ="DELETE FROM interaction WHERE interactionId= ?";
    private static final String UPDATE_VOTE_COMMENT = "UPDATE interaction SET time = NOW() ,type =? WHERE interactionId=?";
    private static final String ADD_INTERACTION_COMMENT = "INSERT INTO interaction (userId,postId,commentId,type,time) VALUES (?,-1,?,?,NOW())";

    public int getNumVoteComment(int id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement voteUp = connection.prepareStatement(COUNT_VOTE_UP_COMMENT);
        voteUp.setString(1,String.valueOf(id));
        ResultSet rs=voteUp.executeQuery();
        int cntVoteUp=0,cntVoteDown=0;
        if(rs.next()) {
            cntVoteUp = rs.getInt("count(DISTINCT interactionId)");
        }
        PreparedStatement voteDown = connection.prepareStatement(COUNT_VOTE_DOWN_COMMENT);
        voteDown.setString(1,String.valueOf(id));
        ResultSet rs1= voteDown.executeQuery();
        if(rs1.next()){
            cntVoteDown= rs1.getInt("count(DISTINCT interactionId)");
        }
        return cntVoteUp - cntVoteDown;
    }
    private Interaction getInteractionComment(int commentId, int userId) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps= connection.prepareStatement(GET_INTERACTION_COMMENT);
        ps.setString(1,String.valueOf(commentId));
        ps.setString(2,String.valueOf(userId));
        ResultSet rs = ps.executeQuery();
        Interaction vote =new Interaction(); // chưa vote

        if(rs.next()){
            vote.setInteractionId(rs.getInt("interactionId"));
            vote.setUserId(rs.getInt("userId"));
            vote.setPostId(rs.getInt("postId"));
            vote.setType(rs.getString("type"));
            vote.setTime(rs.getTimestamp("time"));
        }
        return vote;
    }
    public int getStateVoteComment(int commentId, int userId) throws SQLException {
        Interaction vote = getInteractionComment(commentId,userId);
        if(vote.getType() == null) return 0;
        int stateVote=0;
        if(vote.getType().equals("up")){
            stateVote=1;
        }
        else stateVote=-1;
        return stateVote;
    }
    public void addInteractionComment(int commentId, int userId, String type) throws SQLException {
            Interaction vote= getInteractionComment(commentId,userId);
            Connection connection =getConnection();
            if(vote.getType()== null){
                PreparedStatement addVote = connection.prepareStatement(ADD_INTERACTION_COMMENT);
                addVote.setString(1,String.valueOf(userId));
                addVote.setString(2,String.valueOf(commentId));
                addVote.setString(3,type);
                addVote.execute();
            }
            else {
                if(type.equals(vote.getType())) {
                    PreparedStatement delete = connection.prepareStatement(DELETE_INTERACTION_COMMENT);
                    delete.setString(1, String.valueOf(vote.getInteractionId()));
                    delete.execute();
                }
                else {
                    PreparedStatement update = connection.prepareStatement(UPDATE_VOTE_COMMENT);
                    update.setString(1,type);
                    update.setString(2,String.valueOf(vote.getInteractionId()));
                    update.execute();
                }
            }
    }
    private static final String GET_VIEW = "SELECT * FROM interaction WHERE postId=? AND userId=? AND type = 'view'";
    public void addInteractionView(Interaction x) {
        try{
            Connection connection = getConnection();
            PreparedStatement ps= connection.prepareStatement(GET_VIEW);
            ps.setString(1,String.valueOf(x.getPostId()));
            ps.setString(2,String.valueOf(x.getUserId()));
            ResultSet rs=ps.executeQuery();
            if(!rs.next()){
                PreparedStatement ps2= connection.prepareStatement(ADD_INTERACTION);
                Interaction y = new Interaction();
                ps2.setString(1,String.valueOf(x.getUserId()));
                ps2.setString(2,String.valueOf(x.getPostId()));
                ps2.setString(3,x.getType());
                ps2.execute();
                ps2.close();
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
