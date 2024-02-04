package crazy_selling_store.service.impl;

import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.Ad;
import crazy_selling_store.entity.User;
import crazy_selling_store.repository.AdRepository;
import crazy_selling_store.repository.UserRepository;
import crazy_selling_store.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

    // выполнено
    public crazy_selling_store.dto.ads.Ad createAd(CreateOrUpdateAd properties,
                                                   MultipartFile image,
                                                   Authentication authentication) throws IOException {
///////////////////////////////////////////////////
        Ad newAd = INSTANCE.toEntityAd(properties);
///////////////////////////////////////////
        User userFromDB = null;
        try {
            userFromDB = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        newAd.setUser(userFromDB);
////////////////////////////////////////////////////////////////////////
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
        newAd.setImage(imageDir);
        adRepository.save(newAd);
        return INSTANCE.toDTOAd(newAd);
    }

    // выполнено
    public Ads getAllAds() {
        List<crazy_selling_store.dto.ads.Ad> adsList = new ArrayList<>();
        for (Ad ad : adRepository.findAll()) {
            adsList.add(INSTANCE.toDTOAd(ad));
        }
        return new Ads(adsList.size(), adsList);

    }

    //    выполнено
    public Ads getAuthUserAds(Authentication authentication) {
        User userFromDB = null;
        try {
            userFromDB = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не зарегистрирован"));
        } catch (UsernameNotFoundException e) {
            log.info("Пользователь не зарегистрирован");
        }
        List<crazy_selling_store.dto.ads.Ad> adsList = new ArrayList<>();
        for (Ad ad : adRepository.findByUser(userFromDB)) {
            adsList.add(INSTANCE.toDTOAd(ad));
        }
        return new Ads(adsList.size(), adsList);
    }


    public ExtendedAd getAdFullInfo(Integer id) {
        log.info("2");
        Ad ad = adRepository.getAdByPk(id);
        log.info("3");
        User user = ad.getUser();
        log.info("4");
        return INSTANCE.toDTOExtendedAd(user, ad);
    }
}

