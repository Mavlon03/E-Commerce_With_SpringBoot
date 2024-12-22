package uz.pdp.e_commerce_with_springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import uz.pdp.e_commerce_with_springboot.entity.*;
import uz.pdp.e_commerce_with_springboot.repo.AttachmentContentRepository;
import uz.pdp.e_commerce_with_springboot.repo.CategoryRepository;
import uz.pdp.e_commerce_with_springboot.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {
    private final ProductRepository productRepository;
    private final AttachmentContentRepository attachmentContentRepository;
    private final CategoryRepository categoryRepository;
    @GetMapping("/menu")
    public String menuPage(@RequestParam(required = false) Integer categoryId,
                           Model model,
                           @SessionAttribute(value = "currentUser", required = false) User currentUser,
                           @SessionAttribute(value = "basket", required = false) List<Basket> basket) {
        List<Category> categories = categoryRepository.findAll();
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findAllByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("basket", basket != null ? basket : new ArrayList<Basket>());
        model.addAttribute("currentUser", currentUser);
        return "menu";
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
        if (attachmentContent != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(attachmentContent.getPhoto());
        }
        return ResponseEntity.notFound().build();
    }
}
