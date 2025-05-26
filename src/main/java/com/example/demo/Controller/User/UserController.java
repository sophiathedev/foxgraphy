package com.example.demo.Controller.User;

import com.example.demo.DAO.*;
import com.example.demo.Model.Post;
import com.example.demo.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private FollowDAO followDAO;
    @Autowired
    private NotificationDAO notificationDAO;
    @Autowired
    private PostDAO postDAO;
    @Autowired
    private InteractionDAO interactionDAO;
    @Autowired
    private CommentDAO commentDAO;
    @GetMapping("/user/{id}/post")
    public String user_post(ModelMap modelMap, @PathVariable int id, HttpSession httpSession) throws SQLException {
        Integer idd = (Integer) httpSession.getAttribute("userId");
        if (idd == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user2=  userDAO.getUserByUserId(id);
        user2.setCountFollow(followDAO.countFollow(user2.getUserId()));
        User user= userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        int stateFollow = followDAO.getStateFollow(user.getUserId(), user2.getUserId());
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        List<Post> posts= postDAO.getPostsByUserId(user2.getUserId());
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        modelMap.addAttribute("posts",posts);
        modelMap.addAttribute("userId",idd);
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("stateFollow",stateFollow);
        modelMap.addAttribute("user2",user2);
        modelMap.addAttribute("user",user);
        return "user_post";
    }
    @GetMapping("user/{id}/question")
    public String user_question(ModelMap modelMap, @PathVariable int id, HttpSession httpSession) throws SQLException {
        Integer idd = (Integer) httpSession.getAttribute("userId");
        if (idd == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user2=  userDAO.getUserByUserId(id);
        user2.setCountFollow(followDAO.countFollow(user2.getUserId()));
        User user= userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        int stateFollow = followDAO.getStateFollow(user.getUserId(), user2.getUserId());
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        List<Post> posts= postDAO.getQuestionsByUserId(user2.getUserId());
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        modelMap.addAttribute("userId",idd);
        modelMap.addAttribute("posts",posts);
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("stateFollow",stateFollow);
        modelMap.addAttribute("user2",user2);
        modelMap.addAttribute("user",user);
        return "user_question";
    }
    @GetMapping("user/{id}/contact")
    public String user_contact(ModelMap modelMap, @PathVariable int id, HttpSession httpSession) throws SQLException {
        Integer idd = (Integer) httpSession.getAttribute("userId");
        if (idd == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user2=  userDAO.getUserByUserId(id);
        user2.setCountFollow(followDAO.countFollow(user2.getUserId()));
        User user= userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        int stateFollow = followDAO.getStateFollow(user.getUserId(), user2.getUserId());
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("stateFollow",stateFollow);
        modelMap.addAttribute("user2",user2);
        modelMap.addAttribute("user",user);
        modelMap.addAttribute("userId",idd);
        return "user_contact";
    }
}
