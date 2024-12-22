package uz.pdp.e_commerce_with_springboot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginReq {
   private String phone;
   private String password;
}
