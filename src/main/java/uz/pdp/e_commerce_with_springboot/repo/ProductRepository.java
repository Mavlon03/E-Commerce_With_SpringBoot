package uz.pdp.e_commerce_with_springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.e_commerce_with_springboot.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategoryId(Integer categoryId);
}