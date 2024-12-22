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
public class Product extends BaseEntity {
    private String name;
    private Integer price;
    private Integer amount;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Attachment productPhoto;
}
