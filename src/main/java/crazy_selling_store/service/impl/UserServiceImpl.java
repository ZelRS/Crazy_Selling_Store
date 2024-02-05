package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.entity.User;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static crazy_selling_store.mapper.UserMapper.INSTANCE;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        User userFromDB;
        try {
            userFromDB = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return false;
        }
        userFromDB.setPassword(encoder.encode(newPassword.getNewPassword()));
        repository.save(userFromDB);
        return true;
    }

    @Override
    public crazy_selling_store.dto.users.User getUserInformation(Authentication authentication) {
        User userFromDB;
        try {
            userFromDB = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return null;
        }
        return INSTANCE.toDTOUser(userFromDB);
    }

    @Transactional
    @Override
    public UpdateUser updateUserInfo(UpdateUser updateUser, Authentication authentication) {
        User userFromDB;
        try {
            userFromDB = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return null;
        }
        userFromDB.setFirstName(updateUser.getFirstName());
        userFromDB.setLastName(updateUser.getLastName());
        userFromDB.setPhone(updateUser.getPhone());
        repository.save(userFromDB);
        return updateUser;
    }

    @Transactional
    @Override
    public boolean updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException {
        User userFromDB;
        try {
            userFromDB = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
            return false;

        }
        String imageDir = "src/main/resources/userAvatars";
        String origFilename = image.getOriginalFilename();
        assert origFilename != null;
        assert userFromDB != null;
        String savedFileName = userFromDB.getEmail() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));
        Path filePath = Path.of(imageDir, savedFileName);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        userFromDB.setImage(imageDir + savedFileName);
        repository.save(userFromDB);
        return true;
    }
}
