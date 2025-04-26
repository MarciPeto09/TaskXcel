package Marcelina.example.TaskXcel.controller;

import Marcelina.example.TaskXcel.dto.RequestUserDto;
import Marcelina.example.TaskXcel.dto.ResponseUserDto;
import Marcelina.example.TaskXcel.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/logIn")
public class LoginController {

    @Autowired
    private  UsersService usersService;


    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("user", new RequestUserDto());
        return "LoginPage";
    }


    @PostMapping
    public String login(@ModelAttribute("user") RequestUserDto user,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        ResponseUserDto authenticatedUser = usersService.findByUsernameAndPasswordAndRole(user.getUsername(), user.getPassword(), user.getRole());

        if (authenticatedUser != null) {
            session.setAttribute("currentUser", user);
            session.setAttribute("employeeId", authenticatedUser.getId());
            switch (user.getRole()) {
                case "ADMIN":
                    return "redirect:/users";
                case "MANAGER":
                    return "redirect:/dashboard";
                case "EMPLOYEE":
                    return "redirect:/tasks";
                default:
                    redirectAttributes.addFlashAttribute("error", "Invalid role");
                    return "redirect:/error/404";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/error/500";
        }
    }

}
