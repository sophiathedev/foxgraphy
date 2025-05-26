package com.example.demo.Controller.Admin;

import com.example.demo.DAO.CommentDAO;
import com.example.demo.DAO.PostDAO;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DAO.ReportDAO;

@Controller
public class ReportControllerA {
    @Autowired
    ReportDAO reportDAO;
    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private PostDAO postDAO;
    @RequestMapping("/admin_report")
    public String Report(Model model){
        model.addAttribute("reports" , reportDAO.getAllReport());
        return "admin/Report";
    }

    @RequestMapping("/delete-report")
    public String deleteUser(Model model , @RequestParam("id") String reportId) throws Exception {
        reportDAO.deleteReportById(reportId);
        return "redirect:/admin_report";
    }

    @RequestMapping("/report-view/{id}")
    public String ReportView(Model model,@RequestParam("postId") int postId,@RequestParam("commentId") int commentId, @PathVariable("id") int reportId) throws Exception {

        model.addAttribute("report", reportDAO.selectReportById(reportId));
        if(postId==-1){
            Comment comment=commentDAO.selectCommentById(commentId);
            model.addAttribute("type","Comment");
            model.addAttribute("comment", comment);
        }
        else {
            Post post= postDAO.getPostById(postId);
            model.addAttribute("type","post");
            model.addAttribute("post",post);
        }
        return "admin/report-view";
    }
}