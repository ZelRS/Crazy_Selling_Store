package crazy_selling_store.service.impl;

import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.entity.UserEntity;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static crazy_selling_store.mapper.UserMapper.INSTANCE;

//сервисный класс для обработки пользователей
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UploadImageService uploadImageService;

    @Transactional
    @Override
    public boolean setPassword(NewPassword newPassword, Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            //если такого пользователя нет, ловим исключение и возвращаем из метода false
            log.info("Пользователь не зарегистрирован");
            return false;
        }
        //сеттим в сущность хэшированный пароль
        userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
        //сохраняем пользователя в БД
        repository.save(userEntity);
        return true;
    }

    @Override
    public User getUserInformation(Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            //если такого пользователя нет, ловим исключение и возвращаем из метода null
            log.info("Пользователь не зарегистрирован");
            return null;
        }
        //возвращаем из метода DTO пользователя, образованного от сущности
        return INSTANCE.toDTOUser(userEntity);
    }

    @Transactional
    @Override
    public UpdateUser updateUserInfo(UpdateUser updateUser, Authentication authentication) {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            //если такого пользователя нет, ловим исключение и возвращаем из метода null
            log.info("Пользователь не зарегистрирован");
            return null;
        }
        //сеттим в сущность необходимые поля из DTO
        userEntity.setFirstName(updateUser.getFirstName());
        userEntity.setLastName(updateUser.getLastName());
        userEntity.setPhone(updateUser.getPhone());
        //сохраняем пользователя в БД
        repository.save(userEntity);
        return updateUser;
    }

    @Transactional
    @Override
    public boolean updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity;
        try {
            userEntity = repository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            //если такого пользователя нет, ловим исключение и возвращаем из метода false
            log.info("Пользователь не зарегистрирован");
            return false;

        }
        //формируем строку с путем для хранения аватаров
        String imageDir = "src/main/resources/userAvatars/";
        //формируем строку с оригинальным названием файла
        String origFilename = image.getOriginalFilename();
        //проверяем строку на null
        assert origFilename != null;
        //формируем строку с названием сохраненного файла на сервере
        String savedFileName = userEntity.getEmail() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));
        //формируем путь
        Path filePath = Path.of(imageDir, savedFileName);
        //создаем директорию на сервере
        Files.createDirectories(filePath.getParent());
        //загружаем фото по указанному пути на сервер
        uploadImageService.uploadImage(image, filePath);
        //сеттим URL аватара в сущность
        userEntity.setImage("/" + imageDir + savedFileName);
        //сохраняем пользователя в БД
        repository.save(userEntity);
        return true;
    }
}
