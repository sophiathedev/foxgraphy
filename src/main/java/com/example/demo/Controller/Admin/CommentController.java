package com.example.demo.Controller.Admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DAO.CommentDAO;

@Controller
public class CommentController {

    @Autowired
    CommentDAO commentDAO;

    @RequestMapping("/admin_comment")
    public String Post(Model model) throws SQLException {
        model.addAttribute("comments" , commentDAO.getAllComment());
        return "admin/Comment";
    }
    @GetMapping("/admin_comment/search")
    public String searchPost(@RequestParam("table_search") String id,Model model) throws SQLException {
        int tmp = Integer.parseInt(id);
        List<Comment> comments = commentDAO.getAllComment();
        List<Comment> c = new ArrayList<>();
        for(Comment x: comments){
            if(x.getCommentId()==tmp) c.add(x);
        }
        model.addAttribute("comments", c);
        return "admin/Comment";
    }
    @RequestMapping("/delete-comment")
    public String deleteUser(Model model , @RequestParam("id") String userId) throws Exception {
        commentDAO.deleteCommentById(userId);
        return "redirect:/admin_comment";
    }

    @RequestMapping("/comment-view/{id}")
    public String CommentView(Model model, @PathVariable("id") int commentId) throws Exception {
        model.addAttribute("comment", commentDAO.selectCommentById(commentId));
        return "admin/comment-view";
    }
}