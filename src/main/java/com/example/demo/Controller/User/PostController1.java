package com.example.demo.Controller.User;

import com.example.demo.DAO.*;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Notification;
import com.example.demo.Model.Post;

import com.example.demo.Model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping("")
public class PostController1 {
    static int postId=0;
    static boolean missingTitle=false,missingTags=false,missingContent=false;
    private final PostDAO postDAO;
    private final InteractionDAO interactionDAO;
    private final CommentDAO commentDAO;
    private final UserDAO userDAO;
    private final FollowDAO followDAO;
    private Post post=new Post();
    private Post pre_post=new Post();
    @Autowired
    private NotificationDAO notificationDAO;
    @GetMapping("/")
    public String home(HttpSession httpSession){
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        return "redirect:/post/latest";
    }
    @GetMapping("/post/latest")
    public String allPost(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllPost1();
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("type","post");
        modelMap.addAttribute("index","latest");
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }
    @GetMapping("/post/follow")
    public String followPost(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllFollow(user.getUserId(),"post");
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("type","post");
        modelMap.addAttribute("index","follow");
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }
    @GetMapping("/post/bookmark")
    public String bookmarkPost(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllBookmark(user.getUserId(),"post");
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("type","post");
        modelMap.addAttribute("index","bookmark");
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }
    @GetMapping("/question/latest")
    public String allQuestion(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllQuestion();
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("type","question");
        modelMap.addAttribute("index","latest");
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }
    @GetMapping("/question/follow")
    public String followQuestion(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllFollow(user.getUserId(),"question");
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("type","question");
        modelMap.addAttribute("index","follow");
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }
    @GetMapping("/question/bookmark")
    public String bookmarkQuestion(ModelMap modelMap,HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        List<Post> posts= postDAO.getAllBookmark(user.getUserId(),"question");
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountView(interactionDAO.countView(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("type","question");
        modelMap.addAttribute("index","bookmark");
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("Posts",posts);
        return "listPost";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap, HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        post=pre_post;
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
//        System.out.println(user.getAvatar());
        modelMap.addAttribute("avatarUser", user.getAvatar());
        modelMap.addAttribute("userId", user.getUserId());
        modelMap.addAttribute("Post",post);
        modelMap.addAttribute("missingTitle",missingTitle);
        modelMap.addAttribute("missingTags",missingTags);
        modelMap.addAttribute("missingContent",missingContent);
        return "create";
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("Post") Post postSubmit , BindingResult result, HttpSession httpSession){
        missingContent = result.hasFieldErrors("content");
        missingTags = result.hasFieldErrors("tags");
        missingTitle = result.hasFieldErrors("title");
        if(!result.hasErrors()) {
            if(postDAO.checkExit(postSubmit.getPostId())){
                postDAO.update(postSubmit);
                return "redirect:/"+(postSubmit.getPostId());
            }
            else {
                ++postId;
                postSubmit.setUserId((int)httpSession.getAttribute("userId"));
                postSubmit.setNameAuthor((String)httpSession.getAttribute("username"));
                postDAO.addPost(postSubmit);
                LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
                Timestamp timestamp = Timestamp.valueOf(localDateTime);
                ArrayList<Integer> follower= followDAO.getAllFollower(postSubmit.getUserId());
                for(int x:follower){
                    Notification notice = new Notification();
                    notice.setUserId(x);
                    notice.setPostId(postId);
                    notice.setTime(timestamp);
                    notice.setMessage(String.format("%s wrote a new post",postSubmit.getNameAuthor()));
                    notificationDAO.save(notice);
                    messagingTemplate.convertAndSend("/topic/notifications", notice);
                }
                post=new Post();
                pre_post = new Post();
                return "redirect:/"+(postId);
            }
        }
        else {
            pre_post= postSubmit;
            return "redirect:/create";
        }
    }
    @GetMapping("/{id}")
    public String postDetail(ModelMap modelMap, @PathVariable int id, HttpSession httpSession) throws SQLException,NumberFormatException{
        Integer idx = (Integer) httpSession.getAttribute("userId");
        if (idx == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        post = postDAO.getPostById(id);
        post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        post.setCountView(interactionDAO.countView(post.getPostId()));
        post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));

        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        int vote= interactionDAO.getNumVote(String.valueOf(id));
        int stateVote = interactionDAO.getStateVote(id,user.getUserId());
        int stateBookmark= interactionDAO.getStateBookmark(id,user.getUserId());

        int stateFollow = followDAO.getStateFollow(user.getUserId(),post.getUserId());
        User author = userDAO.getUserByUserId(post.getUserId());
        author.setCountPost(postDAO.countPost(author.getUserId()));
        author.setCountFollow(followDAO.countFollow(author.getUserId()));
        ArrayList<Comment> cmt = commentDAO.getAllCommentByPostId(String.valueOf(id));

        for(Comment c:cmt){
            c.setCountVote(interactionDAO.getNumVoteComment(c.getCommentId()));
            c.setStateVote(interactionDAO.getStateVoteComment(c.getCommentId(),user.getUserId()));
        }
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("Comments",cmt);
        modelMap.addAttribute("author",author);
        modelMap.addAttribute("avatarAuthor",author.getAvatar());
        modelMap.addAttribute("authorID",post.getUserId());
        modelMap.addAttribute("nameAuthor",author.getUsername());
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("vote",vote);
        modelMap.addAttribute("userVote", stateVote);
        modelMap.addAttribute("stateFollow",stateFollow);
        modelMap.addAttribute("post",post);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("username",user.getUsername());
        modelMap.addAttribute("postID",id);
        modelMap.addAttribute("bookmark",stateBookmark);
        String[] tags= post.getTags().split(",");
        modelMap.addAttribute("tags",tags);
        return "postDetail";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String key, ModelMap modelMap, HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        List<Post> posts= postDAO.search(key);
        for(Post post:posts){
            post.setCountBookmark(interactionDAO.countBookmark(post.getPostId()));
            post.setCountVote(interactionDAO.getNumVote(String.valueOf(post.getPostId())));
            post.setCountComment(commentDAO.countNumberComment(post.getPostId()));
        }
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("posts",posts);
        modelMap.addAttribute("userId",user.getUserId());
        modelMap.addAttribute("avatarUser",user.getAvatar());
        return "search";
    }

    @GetMapping("/edit/{id}")
    public String edit(ModelMap modelMap, @PathVariable int id,HttpSession httpSession) throws SQLException {
        Integer idd = (Integer) httpSession.getAttribute("userId");
        if (idd == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        Post post = postDAO.getPostById(id);
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        boolean stateNotice= notificationDAO.checkExitNewNotifications(user.getUserId());
        modelMap.addAttribute("stateNotice",stateNotice);
        modelMap.addAttribute("Post",post);
        modelMap.addAttribute("avatarUser", user.getAvatar());
        modelMap.addAttribute("userId", user.getUserId());
        modelMap.addAttribute("missingTitle",missingTitle);
        modelMap.addAttribute("missingTags",missingTags);
        modelMap.addAttribute("missingContent",missingContent);
        return "create";
    }
    @RequestMapping("/deletePost")
    public String deleteUser(Model model , @RequestParam("id") String userId) throws Exception {
        postDAO.deletePostById(userId);
        return "redirect:/My_Profile/post";
    }


    public PostController1(PostDAO postDAO, InteractionDAO interactionDAO, CommentDAO commentDAO, UserDAO userDAO, FollowDAO followDAO) {
        this.postDAO = postDAO;
        this.interactionDAO = interactionDAO;
        this.commentDAO = commentDAO;
        this.userDAO = userDAO;
        this.followDAO = followDAO;
    }
}