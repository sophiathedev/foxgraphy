package com.example.demo.Controller.User;

import com.example.demo.DAO.NotificationDAO;
import com.example.demo.DAO.UserDAO;
import com.example.demo.Model.Notification;
import com.example.demo.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @PostMapping("/notifications")
    public ResponseEntity<String> createNotification(@RequestBody Notification notification) {
        notificationDAO.save(notification);

        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        // gửi thông báo tới socket
        notification.setTime(timestamp);
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
    @GetMapping("/notifications/{userId}")
    public String getNotifications(@PathVariable("userId") String userId, ModelMap modelMap, HttpSession httpSession) throws SQLException {
        Integer id = (Integer) httpSession.getAttribute("userId");
        if (id == null) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        if( Integer.parseInt(userId)!= id) return "Error";
        List<Notification> notifications = notificationDAO.findByUserId(Integer.parseInt(userId));
        User user = userDAO.getUserByUsername((String) httpSession.getAttribute("username"));
        modelMap.addAttribute("notifications", notifications);
        modelMap.addAttribute("avatarUser",user.getAvatar());
        modelMap.addAttribute("userId",user.getUserId());
        notificationDAO.update(user.getUserId());
        return "notice";
    }
}
