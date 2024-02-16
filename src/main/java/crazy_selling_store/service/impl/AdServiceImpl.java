package crazy_selling_store.service.impl;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static crazy_selling_store.mapper.AdMapper.INSTANCE;

/**
 * Сервисный класс для обработки объявлений.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final UploadImageService uploadImageService;

    /**
     * Создает новое объявление.
     *
     * @param properties     DTO с данными объявления.
     * @param image          Изображение объявления.
     * @param authentication Аутентификация пользователя.
     * @return Созданное объявление.
     * @throws IOException Ошибка при загрузке изображения.
     */
    @Override
    @Transactional
    public Ad createAd(CreateOrUpdateAd properties,
                       MultipartFile image,
                       Authentication authentication) throws IOException {
        // Получаем сущность из DTO.
        AdEntity adEntity = INSTANCE.toEntityAd(properties);
        // Получаем зарегистрированного пользователя по email.
        UserEntity userEntity = null;
        try {
            if (authentication != null) {
                userEntity = userRepository.findUserByEmail(authentication.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
            } else {
                // Если пользователь оказался не зарегистрированным, возвращаем из метода null.
                return null;
            }
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        // Добавляем пользователя к объявлению.
        adEntity.setUser(userEntity);
        // Первично сохраняем объявление в БД для формирования id.
        adRepository.save(adEntity);
        // Формируем строку с путем для хранения фото.
        String imageDir = "src/main/resources/adImages/";
        // Формируем строку с оригинальным названием фото.
        String origFilename = image.getOriginalFilename();
        // Проверяем строку на null.
        assert origFilename != null;
        // Формируем строку с названием сохраненного файла на сервере.
        String imageFinishName = adEntity.getPk() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));
        // Формируем путь.
        Path filePath = Path.of(imageDir, imageFinishName);
        // Создаем директорию на сервере.
        Files.createDirectories(filePath.getParent());
        // Загрузка фото по указанному пути.
        uploadImageService.uploadImage(image, filePath);
        // Сохраняем URL фотографии в таблицу объявления.
        adEntity.setImage("/" + imageDir + imageFinishName);
        // Повторно сохраняем объявление в БД для сеттинга фото.
        adRepository.save(adEntity);
        // Преобразуем сформированные сущности пользователя и объявления в DTO и возвращаем его в метод контроллера.
        return INSTANCE.toDTOAd(adEntity.getUser(), adEntity);
    }

    /**
     * Возвращает все объявления.
     *
     * @return Список объявлений.
     */
    @Override
    public Ads getAllAds() {
        // Формируем список DTO объявлений.
        List<Ad> adsList = new ArrayList<>();
        // Проходим по списку сущностей объявления, полученному из БД, и преобразуем каждое объявление в DTO.
        for (AdEntity ad : adRepository.findAll()) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        // Формируем возвращаем в метод контроллера DTO списка объявлений и их количества(Ads).
        return new Ads(adsList.size(), adsList);
    }

    /**
     * Возвращает объявления авторизованного пользователя.
     *
     * @param authentication Аутентификация пользователя.
     * @return Список объявлений.
     */

    @Override
    public Ads getAuthUserAds(Authentication authentication) {
        // Получаем зарегистрированного пользователя по email.
        UserEntity userEntity = null;
        try {
            userEntity = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        // Формируем список DTO объявлений.
        List<Ad> adsList = new ArrayList<>();
        // Проходим по списку сущностей объявления, полученному из БД по пользователю, и преобразуем каждое объявление в DTO.
        for (AdEntity ad : adRepository.findByUser(userEntity)) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        // Формируем возвращаем в метод контроллера DTO списка объявлений и их количества(Ads).
        return new Ads(adsList.size(), adsList);
    }

    /**
     * Возвращает полную информацию об объявлении.
     *
     * @param id Идентификатор объявления.
     * @return Полная информация об объявлении.
     */
    @Override
    public ExtendedAd getAdFullInfo(Integer id) {
        // Получаем сущность из БД по id.
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            // Если сущность равна null, возвращаем из метода null.
            return null;
        }
        // Получаем пользователя из объявления.
        UserEntity user = ad.getUser();
        // Формируем возвращаем в метод контроллера DTO расширенного просмотра объявления.
        return INSTANCE.toDTOExtendedAd(user, ad);
    }

    /**
     * Удаляет объявление.
     *
     * @param id Идентификатор объявления.
     * @throws IOException Ошибка при удалении изображения.
     */
    @Override
    public void deleteAd(Integer id) throws IOException {
        // Получаем сущность из БД по id.
        AdEntity ad = adRepository.getAdByPk(id);
        // Получаем URL фото из БД.
        String startURL = ad.getImage();
        // С помощью билдера удаляем первый слэш в URL.
        StringBuilder sb = new StringBuilder(startURL);
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        // Формируем корректный путь.
        Path path = Path.of(sb.toString());
        // Удаляем фото с сервера по этому пути.
        Files.delete(path);
        // Удаляем объявление из БД.
        adRepository.deleteByPk(id);
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param id               Идентификатор объявления.
     * @param createOrUpdateAd DTO с данными объявления.
     * @return Обновленное объявление.
     */
    @Override
    public Ad updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        // Получаем сущность из БД по id.
        AdEntity ad = adRepository.getAdByPk(id);
        // Сеттим в сущность необходимые поля из DTO CreateOrUpdateAd.
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setDescription(createOrUpdateAd.getDescription());
        // Сохраняем сущность в БД.
        adRepository.save(ad);
        // Формируем возвращаем в метод контроллера DTO объявления.
        return INSTANCE.toDTOAd(ad.getUser(), ad);
    }

    /**
     * Обновляет фото объявления.
     *
     * @param id    Идентификатор объявления.
     * @param image Новое фото объявления.
     * @return Массив байт нового фото.
     * @throws IOException Ошибка при обновлении фото.
     */
    @Override
    public byte[] updateAdPhoto(Integer id, MultipartFile image) throws IOException {
        // Получаем сущность из БД по id.
        AdEntity ad = adRepository.getAdByPk(id);
        // Получаем URL фото из БД.
        String startURL = ad.getImage();
        // С помощью билдера удаляем первый слэш в URL.
        StringBuilder sb = new StringBuilder(startURL);
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        // Формируем корректный путь.
        Path path = Path.of(sb.toString());
        // загрузка фото по указанному пути
        uploadImageService.uploadImage(image, path);
        //считываем массив байт фотографии и возвращаем его в метод контроллера
        return Files.readAllBytes(path);
    }
}

