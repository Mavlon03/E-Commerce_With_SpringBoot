package uz.pdp.e_commerce_with_springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem extends BaseEntity {

    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;
    private Integer amount;
}
