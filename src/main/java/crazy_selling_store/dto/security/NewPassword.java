package crazy_selling_store.dto.security;

import lombok.Data;

import javax.validation.constraints.Size;

//DTO для изменения пароля пользователя
@Data
public class NewPassword {
    @Size(min = 8, max = 16)
    private String currentPassword;
    @Size(min = 8, max = 16)
    private String newPassword;
}
