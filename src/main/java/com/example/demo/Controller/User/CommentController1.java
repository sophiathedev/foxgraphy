package com.example.demo.Controller.User;

import com.example.demo.DAO.CommentDAO;
import com.example.demo.Model.Comment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class CommentController1 {
    private static int cnt =0;
    private final CommentDAO commentDAO = new CommentDAO();
    @PostMapping("/createCmt")
    public String createCmt(@RequestBody Comment cmt) throws SQLException {
//        System.out.println("hello"+cmt.getContent());
        commentDAO.addComment(cmt);
        //System.out.println("world");
        return "done";
    }
}
