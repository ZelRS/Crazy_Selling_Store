package crazy_selling_store.dto.users;


import crazy_selling_store.dto.enums.Role;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
}
