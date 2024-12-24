package uz.pdp.e_commerce_with_springboot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.e_commerce_with_springboot.entity.Category;
import uz.pdp.e_commerce_with_springboot.entity.Product;
import uz.pdp.e_commerce_with_springboot.entity.Roles;
import uz.pdp.e_commerce_with_springboot.entity.User;
import uz.pdp.e_commerce_with_springboot.repo.CategoryRepository;
import uz.pdp.e_commerce_with_springboot.repo.ProductRepository;
import uz.pdp.e_commerce_with_springboot.repo.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Runnable implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String update;

    @Override
    public void run(String... args) throws Exception {
        if (update.equals("create")){

            User user = new User("Mavlon", "Akmalov", "1", "1", Roles.SUPER, null);
            User user1 = new User("Qalandar", "Qalandarov", "2", "2", Roles.ADMIN, null);
            userRepository.saveAll(List.of(user, user1));

            Category category = new Category("Yegulik");
            Category category1 = new Category("Ichgulik");
            Category category2 = new Category("Kiygulik");
            categoryRepository.saveAll(List.of(category , category1, category2));

            Product product = new Product("Olma", 5000,1,categoryRepository.findById(1).get(), null);
            productRepository.saveAll(List.of(product));
        }
    }
}
