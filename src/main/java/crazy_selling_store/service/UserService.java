package crazy_selling_store.service;

import crazy_selling_store.dto.security.NewPassword;
import org.springframework.security.core.Authentication;

public interface UserService {
    boolean setPassword(NewPassword newPassword, Authentication authentication);
}
