package com.example.demo.Controller.Admin;

import com.example.demo.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DAO.PostDAO;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostDAO postDAO;

    @RequestMapping("/admin_post")
    public String Post(Model model){
            List<Post> posts= postDAO.getAllPost();
            model.addAttribute("posts",posts);
        return "admin/Post";
    }
    @GetMapping("/admin_post/search")
    public String searchPost(@RequestParam("table_search") String id,Model model){
        int tmp = Integer.parseInt(id);
        List<Post> posts= postDAO.getAllPost();
        List<Post> p = new ArrayList<>();
        for(Post x:posts){
            if(x.getPostId()==tmp) p.add(x);
        }
        model.addAttribute("posts", p);
        return "admin/Post";
    }
    @RequestMapping("/delete-post")
    public String deleteUser(Model model , @RequestParam("id") String userId) throws Exception {
        postDAO.deletePostById(userId);
        return "redirect:/admin_post";
    }

    @GetMapping("/post-view/{id}")
    public String PostView(Model model,  @PathVariable("id") int postId) throws Exception {
        model.addAttribute("post", postDAO.getPostById(postId));
        return "admin/post-view";
    }
}