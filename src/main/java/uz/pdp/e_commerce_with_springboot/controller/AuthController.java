package uz.pdp.e_commerce_with_springboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.e_commerce_with_springboot.entity.User;
import uz.pdp.e_commerce_with_springboot.payload.LoginReq;
import uz.pdp.e_commerce_with_springboot.repo.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@ModelAttribute LoginReq loginReq, HttpServletRequest request, Model model) {
        Optional<User> userOptional = userRepository.findByPhoneAndPassword(loginReq.getPhone(), loginReq.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            request.getSession().setAttribute("currentUser", user);  // currentUser sessiyada saqlanadi
            return "redirect:/menu";
        } else {
            model.addAttribute("error", "Invalid phone or password");
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/auth/login";
    }

}
