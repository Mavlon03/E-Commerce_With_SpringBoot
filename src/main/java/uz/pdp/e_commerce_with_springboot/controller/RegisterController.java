package uz.pdp.e_commerce_with_springboot.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.e_commerce_with_springboot.entity.Attachment;
import uz.pdp.e_commerce_with_springboot.entity.AttachmentContent;
import uz.pdp.e_commerce_with_springboot.entity.Roles;
import uz.pdp.e_commerce_with_springboot.entity.User;
import uz.pdp.e_commerce_with_springboot.repo.AttachmentContentRepository;
import uz.pdp.e_commerce_with_springboot.repo.AttachmentRepository;
import uz.pdp.e_commerce_with_springboot.repo.UserRepository;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@MultipartConfig
public class RegisterController {

    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    @GetMapping("/register")
    public String registerPage(){
        return "addUser";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam("photo") MultipartFile photo) throws IOException {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRoles(Roles.USER);
        if (photo != null && !photo.isEmpty()){
            Attachment attachment = new Attachment();
            String originalFilename = photo.getOriginalFilename();
            attachment.setFilename(originalFilename);
            Attachment save = attachmentRepository.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent();
            attachmentContent.setPhoto(photo.getBytes());
            attachmentContent.setAttachment(save);
            attachmentContentRepository.save(attachmentContent);
//            user.setUserPhoto(attachment);
        }
        userRepository.save(user);
        System.out.println("Firstname: " + firstname);
        System.out.println("Lastname: " + lastname);
        System.out.println("Phone: " + phone);
        System.out.println("Password: " + password);
        System.out.println("Photo name: " + photo.getOriginalFilename());
        System.out.println("Photo size: " + photo.getSize());
        System.out.println("Photo type: " + photo.getContentType());
        return "redirect:/auth/login";
    }
}
