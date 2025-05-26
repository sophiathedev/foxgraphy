package com.example.demo.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DAO.UserDAO;
import com.example.demo.DAO.CommentDAO;

@Controller
public class AdminController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    CommentDAO commentDAO;

    @RequestMapping("/admin")
    public String admin(Model model) throws Exception {
        model.addAttribute("users" , userDAO.getAll());
        return "admin/admin";
    }

    @RequestMapping("/admin/delete-user")
    public String deleteUser(Model model , @RequestParam("id") String userId) throws Exception {
        userDAO.deleteUserById(userId);
        return "redirect:/admin";
    }

    @RequestMapping("/admin/hide-comment")
    public String hideComment(Model model, @RequestParam("commentId") int commentId) throws Exception {
        commentDAO.hideCommentById(commentId);
        return "redirect:/admin";
    }
}