package com.example.demo.Controller.User;
import com.example.demo.DAO.*;
import com.example.demo.Model.FileUpload;
import com.example.demo.Model.Post;
import com.example.demo.Model.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/My_Profile")
public class MyProfileController {
    private final UserDAO userDAO;
    private final PostDAO postDAO;
    private final NotificationDAO notificationDAO;
    private final InteractionDAO interactionDAO;
    private final CommentDAO commentDAO;
    private static final String UPLOAD_DIR="static/file/";
    public MyProfileController(UserDAO userDAO, PostDAO postDAO, NotificationDAO notificationDAO, InteractionDAO interactionDAO,CommentDAO commentDAO) {
        this.userDAO = userDAO;
        this.postDAO = postDAO;
        this.notificationDAO=notificationDAO;
        this.interactionDAO=interactionDAO;
        this.commentDAO=commentDAO;
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model, HttpSession session) throws SQLException {
        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUserId(userId);
        boolean stateNotice= notificationDAO.checkExitNewNotifications(userId);
        model.addAttribute("avatar",new FileUpload());
        model.addAttribute("stateNotice",stateNotice);
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        model.addAttribute("avatarUser",user.getAvatar());
        return "userInfo";
    }

    @PostMapping("/userInfo")
    public String updateUserInfo(@ModelAttribute("user") User user, HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        user.setUserId(userId); // Gán userId từ session
        //check exit mail or username
        boolean check =userDAO.checkExitEmail(user.getEmail(),user.getUserId()) ;
        boolean check2 =userDAO.checkExitUsername(user.getUsername(),user.getUserId());
        if(check&check2){
            userDAO.updateUser(user);
            return "redirect:/My_Profile/userInfo";}
        else {
            return "redirect:/My_Profile/userInfo?error=username or email already exists";
        }
    }
    @GetMapping("/question")
    public String question(Model model, HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        List<Post> questions = postDAO.getQuestionsByUserId(userId);
        for(Post post:questions){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        model.addAttribute("posts", questions);
        model.addAttribute("userId", userId);
        model.addAttribute("avatarUser",userId+".png");
        boolean stateNotice= notificationDAO.checkExitNewNotifications(userId);
        model.addAttribute("stateNotice",stateNotice);
        return "profile_question";
    }
    @GetMapping("/post")
    public String postIndex(Model model, HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        List<Post> posts = postDAO.getPostsByUserId(userId);
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(userId);
        model.addAttribute("stateNotice",stateNotice);
        model.addAttribute("posts", posts);
        model.addAttribute("userId", userId);
        model.addAttribute("avatarUser",userId+".png");
        return "profile_post";
    }

    @GetMapping("/ChangePassword")
    public String changePassword(Model model, HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUserId(userId);
        boolean stateNotice= notificationDAO.checkExitNewNotifications(userId);
        model.addAttribute("stateNotice",stateNotice);
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        model.addAttribute("avatarUser",userId+".png");
        return "profile_change_password";
    }
    @PostMapping("/ChangePassword")
    public String handlePasswordChange(
            @RequestParam("password") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        User user = userDAO.getUserByUserId(userId);
        if (!user.getPassword().equals(oldPassword)) {
            return "redirect:/My_Profile/userInfo?error=Old password is incorrect";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/My_Profile/userInfo?error=Passwords do not match";
        }

        user.setPassword(newPassword);
        userDAO.updatePassword(user);

        return "redirect:/My_Profile/userInfo?success=Password changed successfully";
    }
    @PostMapping("/changeAvatar")
    public String updateAvatar(@ModelAttribute("avatar") FileUpload avatar, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        MultipartFile file =avatar.getFile();
        if (file.isEmpty()) {
            return "redirect:/My_Profile/userInfo?error=File is empty";
        }
        else{
            String txt= Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            if(txt.equals("png") || txt.equals("jpg")||  txt.equals(".gif")||txt.equals("jpeg") ) {
                try {
                    String path = "uploads/" + userId + ".png";
                    FileCopyUtils.copy(file.getBytes(), new File(path));

//            userDAO.updateAvatar(userId,"/file/"+file.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                    return "redirect:/My_Profile/userInfo?error=Could not upload file";
                }
                return "redirect:/My_Profile/userInfo?success=Avatar changed successfully";
            }
            else return "redirect:/My_Profile/userInfo?error=The uploaded file is not a valid image format.";
        }

    }

}