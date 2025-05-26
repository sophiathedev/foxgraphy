package com.example.demo.Controller.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.DAO.UserDAO;
import com.example.demo.Model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final UserDAO userDAO;
    private static final String UPLOAD_DIR = "static/file/";
    @GetMapping("/login")
    public String login(ModelMap modelMap){
        modelMap.addAttribute("user",new User());
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("user") User userC, BindingResult result, HttpSession httpSession) throws SQLException {
        boolean missingUsername =result.hasFieldErrors("username");
        boolean missingPassword =result.hasFieldErrors("password");
        if(!missingPassword && !missingUsername){
            User user= userDAO.getUserByUsername(userC.getUsername());
            if(user.getUsername()== null) return "redirect:/login?error=username or password is wrong";
            if( user.getPassword().equals(userC.getPassword())){
                if(user.getRole().equals("user")){
                    httpSession.setAttribute("username",user.getUsername());
                    httpSession.setAttribute("userId",user.getUserId());
                    return "redirect:/post/latest";
                }
                else return "redirect:/admin";
            }
            else return "redirect:/login?error=username or password is wrong";
        }
        else{
            return "redirect:/login/error=username or password is wrong";
        }

    }
    @GetMapping("/signup")
    public String Signup(ModelMap modelMap){

        modelMap.addAttribute("user",new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute("user")User user, BindingResult result,HttpSession httpSession) throws SQLException, IOException {
        boolean check= userDAO.checkUsername(user.getUsername());
        boolean check1= userDAO.checkEmail(user.getEmail());
        if(check&check1) {
            userDAO.addUser(user);
            User user2= userDAO.getUserByUsername(user.getUsername());
            int cnt= user2.getUserId();
            userDAO.updateAvatar(cnt,cnt+".png");
            ClassPathResource resource = new ClassPathResource("static/file/avatar_default.png");
            Path sourcePath= Path.of(resource.getURI());
            Path destinationPath = Paths.get("uploads/"+cnt+".png");
            try{
                if (!Files.exists(destinationPath.getParent())) {
                    Files.createDirectories(destinationPath.getParent());
                }
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e) { e.printStackTrace(); }

            //

            //
            httpSession.setAttribute("username",user2.getUsername());
            httpSession.setAttribute("userId",user2.getUserId());
            return "redirect:/post/latest";
        }
        else {
            System.out.println("Heee");
            return "redirect:/signup?error=email or username already exists";
        }

    }
    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
