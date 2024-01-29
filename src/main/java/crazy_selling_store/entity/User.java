package crazy_selling_store.entity;

import crazy_selling_store.dto.enums.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    @Lob
    private String image;
    private String password;

//    public User(Integer id, String email, String firstName, String lastName, String phone, Role role) {
//        this.id = id;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phone = phone;
//        this.role = role;
//    }
}
