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

    @GetMapping("/basket")
    public String basketPage(@SessionAttribute(value = "basket", required = false) List<Basket> basket, Model model) {
        if (basket == null) {
            basket = new ArrayList<>();
        }
        int basketCount = basket.size();
        model.addAttribute("basketCount", basketCount);
        double totalPrice = 0;
        for (Basket item : basket) {
            totalPrice += item.getProduct().getPrice() * item.getAmount();
        }

        model.addAttribute("basket", basket);
        model.addAttribute("totalPrice", totalPrice);
        return "basket";
    }

    @PostMapping("/addToBasket")
    public String addToBasket(@RequestParam Integer productId, HttpSession session) {
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        if (basket == null) {
            basket = new ArrayList<>();
        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            Basket existingBasket = basket.stream()
                    .filter(b -> b.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);
            if (existingBasket != null) {
                existingBasket.setAmount(existingBasket.getAmount() + 1);
            } else {
                Basket newBasket = new Basket(product, 1);
                basket.add(newBasket);
            }
        }
        session.setAttribute("basket", basket);
        return "redirect:/menu";
    }

    @PostMapping("/removeFromBasket")
    public String removeFromBasket(@RequestParam Integer productId, HttpSession session) {
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");

        if (basket != null) {
            basket.removeIf(item -> item.getProduct().getId().equals(productId));
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }

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
                    if (item.getAmount() < 1) {
                        item.setAmount(1);
                    }
                    basketRepository.save(item);
                    break;
                }
            }
            session.setAttribute("basket", basket);
        }
        return "redirect:/basket";
    }
}
