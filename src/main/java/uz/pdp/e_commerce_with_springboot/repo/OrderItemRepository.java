package uz.pdp.e_commerce_with_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.e_commerce_with_springboot.entity.Order;
import uz.pdp.e_commerce_with_springboot.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrder(Order order);
}