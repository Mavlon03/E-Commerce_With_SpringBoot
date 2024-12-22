package uz.pdp.e_commerce_with_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.e_commerce_with_springboot.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
  Basket findByProductId(Integer productId);
}