package uz.pdp.e_commerce_with_springboot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import uz.pdp.e_commerce_with_springboot.entity.Basket;
import uz.pdp.e_commerce_with_springboot.entity.Product;
import uz.pdp.e_commerce_with_springboot.repo.BasketRepository;
import uz.pdp.e_commerce_with_springboot.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BasketController {
    private final ProductRepository productRepository;
    private final BasketRepository basketRepository;

    // Savatcha sahifasi
    @GetMapping("/basket")
    public String basketPage(@SessionAttribute(value = "basket", required = false) List<Basket> basket, Model model) {
        if (basket == null) {
            basket = new ArrayList<>();
        }
        int basketCount = basket.size();
        model.addAttribute("basketCount", basketCount);
        double totalPrice = 0;
        // Basketdagi har bir mahsulot uchun jami narxni hisoblash
        for (Basket item : basket) {
            totalPrice += item.getProduct().getPrice() * item.getAmount();
        }

        model.addAttribute("basket", basket);
        model.addAttribute("totalPrice", totalPrice);
        return "basket";
    }

    // Savatga mahsulot qo'shish
    @PostMapping("/addToBasket")
    public String addToBasket(@RequestParam Integer productId, HttpSession session) {
        // Sessiyadagi basketni olish
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            // Basketda mahsulot bormi, tekshirish
            Basket existingBasket = basket.stream()
                    .filter(b -> b.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            // Agar mahsulot savatda bo'lsa, uning miqdorini oshirish
            if (existingBasket != null) {
                existingBasket.setAmount(existingBasket.getAmount() + 1);
            } else {
                // Yangi mahsulotni savatga qo'shish
                Basket newBasket = new Basket(product, 1);
                basket.add(newBasket);
            }
        }
        // Sessiyada basketni yangilash
        session.setAttribute("basket", basket);
        return "redirect:/menu";
    }

    // Savatchadan mahsulotni olib tashlash
    @PostMapping("/removeFromBasket")
    public String removeFromBasket(@RequestParam Integer productId, HttpSession session) {
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");

        if (basket != null) {
            // Mahsulotni savatdan olib tashlash
            basket.removeIf(item -> item.getProduct().getId().equals(productId));
            // Sessiyada basketni yangilash
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }

    // Savatchadagi mahsulotning miqdorini yangilash
    @PostMapping("/updateBasket")
    public String updateBasket(@RequestParam Integer productId,
                               @RequestParam String action,
                               HttpSession session) {
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        if (basket != null) {
            for (Basket item : basket) {
                if (item.getProduct().getId().equals(productId)) {
                    if ("decrement".equals(action)) {
                        item.setAmount(item.getAmount() - 1);
                    } else if ("increment".equals(action)) {
                        item.setAmount(item.getAmount() + 1);
                    }
                    // Mahsulot miqdori 1 dan kam bo'lishi mumkin emas
                    if (item.getAmount() < 1) {
                        item.setAmount(1);
                    }
                    basketRepository.save(item);
                    break;
                }
            }
            // Sessiyada basketni yangilash
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }
}
