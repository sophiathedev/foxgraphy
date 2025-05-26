package com.example.demo.Controller.User;

import com.example.demo.DAO.FollowDAO;
import com.example.demo.Model.Follow;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/follow")
public class FollowController {
    private static int cnt=0;
    private final FollowDAO followDAO;
    public FollowController(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }
    @PostMapping("/delete")
    public String unfollow(@RequestBody Follow follow) throws SQLException {
        followDAO.delete(follow.getUserIdSrc(),follow.getUserIdDst());
        return "done";
    }
    @PostMapping("/insert")
    public String follow(@RequestBody Follow follow) throws SQLException {
        follow.setFollowId(++cnt);
        followDAO.insert(follow.getUserIdSrc(),follow.getUserIdDst());
        return "done";
    }
}
