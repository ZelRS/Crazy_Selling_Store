package crazy_selling_store.service.impl;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import crazy_selling_store.exceptions.EntityNotFoundException;
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
        AdEntity adEntity = INSTANCE.toEntityAd(properties);
        UserEntity userFromDB = null;
        try {
            if (authentication != null) {
                userFromDB = userRepository.findUserByEmail(authentication.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
            } else {
                return null;
            }
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        adEntity.setUser(userFromDB);
        String imageDir = "src/main/resources/adImages";
        String origFilename = image.getOriginalFilename();
        assert origFilename != null;
        Path filePath = Path.of(imageDir, properties.getTitle() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1)));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        adEntity.setImage(imageDir + "/" + properties.getTitle() + "." +
                Objects.requireNonNull(origFilename.substring(origFilename.lastIndexOf(".") + 1)));
        adRepository.save(adEntity);
        return INSTANCE.toDTOAd(adEntity.getUser(), adEntity);
    }

    @Override
    public Ads getAllAds() {
        List<crazy_selling_store.dto.ads.Ad> adsList = new ArrayList<>();
        for (AdEntity ad : adRepository.findAll()) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        return new Ads(adsList.size(), adsList);
    }

    @Transactional
    @Override
    public Ads getAuthUserAds(Authentication authentication) {
        UserEntity userFromDB = null;
        try {
            userFromDB = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        List<crazy_selling_store.dto.ads.Ad> adsList = new ArrayList<>();
        for (AdEntity ad : adRepository.findByUser(userFromDB)) {
            adsList.add(INSTANCE.toDTOAd(ad.getUser(), ad));
        }
        return new Ads(adsList.size(), adsList);
    }

    @Override
    public ExtendedAd getAdFullInfo(Integer id) {
        AdEntity ad = adRepository.getAdByPk(id);
        if (ad == null) {
            return null;
        }
        UserEntity user = ad.getUser();
        return INSTANCE.toDTOExtendedAd(user, ad);
    }

    @Transactional
    @Override
    public void deleteAd(Integer id) throws IOException {
        AdEntity ad = adRepository.getAdByPk(id);
        Path path = Path.of(ad.getImage());
        Files.delete(path);
        adRepository.deleteByPk(id);
    }

//    необходимо добавить логику переименования изображения и пути до файла, чтобы они корректно удалялись с сервера
    @Transactional
    @Override
    public Ad updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        AdEntity ad = adRepository.getAdByPk(id);



        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setDescription(createOrUpdateAd.getDescription());
        adRepository.save(ad);
        return INSTANCE.toDTOAd(ad.getUser(), ad);
    }

    @Override
    public byte[] updateAdPhoto(Integer id, MultipartFile image) throws IOException {
        AdEntity ad = adRepository.getAdByPk(id);
        String fileURL = ad.getImage();
        Path path = Path.of(fileURL);
        Files.deleteIfExists(path);

        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(path, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        return Files.readAllBytes(path);
    }

    public boolean isAdAuthor(String username, Integer id) {
            AdEntity ad = adRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
        return ad.getUser().getEmail().equals(username);
    }
}

