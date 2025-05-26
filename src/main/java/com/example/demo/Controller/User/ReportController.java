package com.example.demo.Controller.User;

import com.example.demo.DAO.ReportDAO;
import com.example.demo.Model.Report;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class ReportController {
    private static  int cnt=0;
    private final ReportDAO reportDAO;
    public ReportController(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }
    @PostMapping("/sendReport")
    public String addReport(@RequestBody Report report) throws SQLException {
        reportDAO.addReport(report);
        return "done";
    }
}
