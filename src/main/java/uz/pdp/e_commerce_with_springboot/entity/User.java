package uz.pdp.e_commerce_with_springboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles roles = Roles.USER;
    @ManyToOne
    private Attachment userPhoto;

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
