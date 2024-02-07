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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static crazy_selling_store.mapper.AdMapper.INSTANCE;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Ad createAd(CreateOrUpdateAd properties,
                       MultipartFile image,
                       Authentication authentication) throws IOException {
        //получаем сущность из DTO
        AdEntity adEntity = INSTANCE.toEntityAd(properties);
        //получаем зарегистрированного пользователя по email
        UserEntity userFromDB = null;
        try {
            if (authentication != null) {
                userFromDB = userRepository.findUserByEmail(authentication.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
            } else {
                //если пользователь оказался не зарегистрированным возвращаем из метода null
                return null;
            }
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        //добавляем пользователя к объявлению
        adEntity.setUser(userFromDB);
        //формируем директорию и имя для хранения загружаемой фотографии объявления
        String imageDir = "src/main/resources/adImages";
        String origFilename = image.getOriginalFilename();
        assert origFilename != null;
        String imageFinishName = properties.getTitle() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1));
        Path filePath = Path.of(imageDir, imageFinishName);
        Files.createDirectories(filePath.getParent());
        // загрузка фото по указанному пути
        uploadImage(image, filePath);
        // сохраняем URL фотографии в таблицу объявления
        adEntity.setImage(imageDir + "/" + imageFinishName);
        //сохраняем объявление в БД
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
        UserEntity userFromDB = null;
        try {
            userFromDB = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        //формируем список DTO объявлений
        List<Ad> adsList = new ArrayList<>();
        // проходим по списку сущностей объявления, полученному из БД по пользователю и преобразуем каждое объявление в DTO
        for (AdEntity ad : adRepository.findByUser(userFromDB)) {
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
        Path path = Path.of(ad.getImage());
        //удаляем фото с сервера по URL
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
        Path path = Path.of(ad.getImage());
        // загрузка фото по указанному пути
        uploadImage(image, path);
        //считываем массив байт фотографии и возвращаем его в метод контроллера
        return Files.readAllBytes(path);
    }

    //метод загрузки фото по указанному пути на сервер
    private void uploadImage(MultipartFile image, Path path) throws IOException {
        //если фото по этому пути с таким именем уже существует, то оно удаляется
        Files.deleteIfExists(path);
        //try с ресурсами с буферизацией загружает фото по заданному пути и автоматически закрывает потоки
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
    }
}

