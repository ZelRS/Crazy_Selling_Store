package crazy_selling_store.dto.security;

import crazy_selling_store.dto.enums.Role;
import lombok.Data;

@Data
public class Register {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
}
