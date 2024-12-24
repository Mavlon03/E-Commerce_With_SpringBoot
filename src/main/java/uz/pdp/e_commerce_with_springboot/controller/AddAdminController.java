package uz.pdp.e_commerce_with_springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import uz.pdp.e_commerce_with_springboot.entity.Roles;
import uz.pdp.e_commerce_with_springboot.entity.User;
import uz.pdp.e_commerce_with_springboot.repo.UserRepository;

@Controller
@RequiredArgsConstructor
public class AddAdminController {

    private final UserRepository userRepository;

    @GetMapping("/addAdmin")
    public String addAdminPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "addAdmin";
    }

    @PostMapping("/addAdmin")
    public String addAdmin(Integer userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        boolean isAdminAdded = false;

        if (user != null && !user.getRoles().equals(Roles.SUPER)) {
            if (user.getRoles() != Roles.ADMIN) {
                user.setRoles(Roles.ADMIN);
                isAdminAdded = true;
            } else if (user.getRoles() == Roles.ADMIN) {
                user.setRoles(Roles.USER);
                isAdminAdded = false;
            }
            userRepository.save(user);
        }

        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("isAdminAdded", isAdminAdded);
        return "addAdmin";
    }
}
