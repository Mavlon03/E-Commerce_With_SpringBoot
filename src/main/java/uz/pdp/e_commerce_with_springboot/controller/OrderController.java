package uz.pdp.e_commerce_with_springboot.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.e_commerce_with_springboot.entity.*;
import uz.pdp.e_commerce_with_springboot.repo.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BasketRepository basketRepository;
    @PostMapping("/createOrder")
    public String createOrder(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/auth/login";
        }
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        if (basket == null || basket.isEmpty()) {
            return "redirect:/basket";
        }
        Order order = new Order();
        order.setName("Order by " + user.getFullName());
        order.setUserId(user);
        order.setDateTime(LocalDateTime.now());
        orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        for (Basket item : basket) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setAmount(item.getAmount());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
        basketRepository.deleteAll(basket);
        session.removeAttribute("basket");
        return "redirect:/order";
    }
    @GetMapping("/order")
    public String getOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Order> orders = orderRepository.findAllByUserId(user);
        model.addAttribute("orders", orders);
        return "order";
    }

    @GetMapping("/orderItem")
    public String getOrderItems(@RequestParam Integer orderId, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.getUserId().getId().equals(currentUser.getId())) {
            return "redirect:/order";
        }
        List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("order", order);
        for (OrderItem orderItem : orderItems) {
            double cashback = orderItem.getProduct().getPrice() * 0.01 * orderItem.getAmount();
            cashback = Math.round(cashback * 100.0) / 100.0;
            String qrText = "Cashback: " + cashback + " \n UZS for product: " + orderItem.getProduct().getName();

            try {
                byte[] qrImageBytes = QRCodeGenerator.generateQRCodeToBytes(qrText, 200, 200);
                String base64Qr = Base64.getEncoder().encodeToString(qrImageBytes);
                orderItem.setQrBase64("data:image/png;base64," + base64Qr);
            } catch (Exception e) {
                e.printStackTrace();
                orderItem.setQrBase64("");
            }
        }
        return "orderItem";
    }



}
