package crazy_selling_store.dto.security;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * DTO для входа пользователя
 */
@Data
public class Login {
    @Size(min = 8, max = 16)
    private String username;
    @Size(min = 4, max = 32)
    private String password;
}
