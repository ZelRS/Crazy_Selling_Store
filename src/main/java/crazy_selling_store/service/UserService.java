package crazy_selling_store.service;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    boolean setPassword(NewPassword newPassword, Authentication authentication);

    User getUserInformation(Authentication authentication);

    UpdateUser updateUserInfo(UpdateUser updateUser, Authentication authentication);

    void updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException;
}
