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

//сервисный класс для обработки объявлений
@Service
@Slf4j
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final UploadImageService uploadImageService;

    @Transactional
    @Override
    public Ad createAd(CreateOrUpdateAd properties,
                       MultipartFile image,
                       Authentication authentication) throws IOException {
        //получаем сущность из DTO
        AdEntity adEntity = INSTANCE.toEntityAd(properties);
        //получаем зарегистрированного пользователя по email
        UserEntity userEntity = null;
        try {
            if (authentication != null) {
                userEntity = userRepository.findUserByEmail(authentication.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
            } else {
                //если пользователь оказался не зарегистрированным возвращаем из метода null
                return null;
            }
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        //добавляем пользователя к объявлению
        adEntity.setUser(userEntity);
        //первично сохраняем объфвление в БД для формирования id
        adRepository.save(adEntity);
        //формируем строку с путем для хранения фото
        String imageDir = "src/main/resources/adImages/";
        //формируем строку с оригинальным названием фото
        String origFilename = image.getOriginalFilename();
        //проверяем строку на null
        assert origFilename != null;
        //формируем строку с названием сохраненного файла на сервере
        String imageFinishName = adEntity.getPk() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));
        //формируем путь
        Path filePath = Path.of(imageDir, imageFinishName);
        //создаем директорию на сервере
        Files.createDirectories(filePath.getParent());
        // загрузка фото по указанному пути
        uploadImageService.uploadImage(image, filePath);
        // сохраняем URL фотографии в таблицу объявления
        adEntity.setImage("/" + imageDir + imageFinishName);
        //повторно сохраняем объвление в БД для сеттинга фото
        adRepository.save(adEntity);
        //преобразуем сформированные сущности пользователя и объявления в DTO и возвращаем его в метод контроллера
        return INSTANCE.toDTOAd(adEntity.getUser(), adEntity);
    }

    @Override
    public Ads getAllAds() {
        //формируем список DTO объявлений
        List<Ad> adsList = new ArrayList<>();
        // проходим по списку сущностей объявления, полученному из БД и преобразуем каждое объявление в DTO
        for (AdEntity ad : adRepository.findAll()) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        //формируем возвращаем в метод контроллера DTO списка объявлений и их количества(Ads)
        return new Ads(adsList.size(), adsList);
    }

    @Transactional
    @Override
    public Ads getAuthUserAds(Authentication authentication) {
        //получаем зарегистрированного пользователя по email
        UserEntity userEntity = null;
        try {
            userEntity = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        //формируем список DTO объявлений
        List<Ad> adsList = new ArrayList<>();
        // проходим по списку сущностей объявления, полученному из БД по пользователю и преобразуем каждое объявление в DTO
        for (AdEntity ad : adRepository.findByUser(userEntity)) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        //формируем возвращаем в метод контроллера DTO списка объявлений и их количества(Ads)
        return new Ads(adsList.size(), adsList);
    }

    @Override
    public ExtendedAd getAdFullInfo(Integer id) {
        //получаем сущность из БД по id
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            //если сущность равна null, возвращаем из метода null
            return null;
        }
        //получаем пользователя из объявления
        UserEntity user = ad.getUser();
        //формируем возвращаем в метод контроллера DTO расширенного просмотра объявления
        return INSTANCE.toDTOExtendedAd(user, ad);
    }

    @Transactional
    @Override
    public void deleteAd(Integer id) throws IOException {
        //получаем сущность из БД по id
        AdEntity ad = adRepository.getAdByPk(id);
        //получаем URL фото из БД
        String startURL = ad.getImage();
        //с помощью билдера удаляем первый слэш в URL
        StringBuilder sb = new StringBuilder(startURL);
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        //формируем корректный путь
        Path path = Path.of(sb.toString());
        //удаляем фото с сервера по этому пути
        Files.delete(path);
        //удаляем объявление из БД
        adRepository.deleteByPk(id);
    }


    //МЕТОД ДОРАБАТЫВАЕТСЯ
    @Transactional
    @Override
    public Ad updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        //получаем сущность из БД по id
        AdEntity ad = adRepository.getAdByPk(id);

        // !!! тут необходимо добавить логику переименования изображения и пути до файла, чтобы они корректно удалялись с сервера

        // сеттим в сущность необходимые поля из DTO CreateOrUpdateAd
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setDescription(createOrUpdateAd.getDescription());
        //сохраняем сущность в БД
        adRepository.save(ad);
        //формируем возвращаем в метод контроллера DTO объявления
        return INSTANCE.toDTOAd(ad.getUser(), ad);
    }

    @Override
    public byte[] updateAdPhoto(Integer id, MultipartFile image) throws IOException {
        //получаем сущность из БД по id
        AdEntity ad = adRepository.getAdByPk(id);
        //получаем URL фото из БД
        String startURL = ad.getImage();
        //с помощью билдера удаляем первый слэш в URL
        StringBuilder sb = new StringBuilder(startURL);
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        //формируем корректный путь
        Path path = Path.of(sb.toString());
        // загрузка фото по указанному пути
        uploadImageService.uploadImage(image, path);
        //считываем массив байт фотографии и возвращаем его в метод контроллера
        return Files.readAllBytes(path);
    }
}

