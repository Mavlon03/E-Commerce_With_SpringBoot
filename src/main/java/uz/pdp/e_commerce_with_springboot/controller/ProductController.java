package uz.pdp.e_commerce_with_springboot.controller;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.e_commerce_with_springboot.entity.Attachment;
import uz.pdp.e_commerce_with_springboot.entity.AttachmentContent;
import uz.pdp.e_commerce_with_springboot.entity.Category;
import uz.pdp.e_commerce_with_springboot.entity.Product;
import uz.pdp.e_commerce_with_springboot.repo.AttachmentContentRepository;
import uz.pdp.e_commerce_with_springboot.repo.AttachmentRepository;
import uz.pdp.e_commerce_with_springboot.repo.CategoryRepository;
import uz.pdp.e_commerce_with_springboot.repo.ProductRepository;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@MultipartConfig
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentContentRepository attachmentContentRepository;

    @GetMapping("/addProduct")
    public String addProductPage(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam String name,
                             @RequestParam Integer price,
                             @RequestParam Integer categoryId,
                             @RequestParam("image") MultipartFile image) throws IOException {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setAmount(1);
            product.setCategory(category);

            if (image != null && !image.isEmpty()) {
                // Faylni saqlash
                Attachment attachment = new Attachment();
                attachment.setFilename(image.getOriginalFilename());
                attachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent();
                attachmentContent.setPhoto(image.getBytes());
                attachmentContent.setAttachment(attachment);
                attachmentContentRepository.save(attachmentContent);

                product.setProductPhoto(attachment);
            }

            productRepository.save(product);
        }
        return "redirect:/menu";
    }


}