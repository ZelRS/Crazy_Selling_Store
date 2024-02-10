package crazy_selling_store.service;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//интерфейс сервиса для обработки объявлений
public interface AdService {
    @Transactional
    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    Ads getAllAds();

    @Transactional
    Ads getAuthUserAds(Authentication authentication);

    ExtendedAd getAdFullInfo(Integer id);

    @Transactional
    void deleteAd(Integer id) throws IOException;

    @Transactional
    Ad updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd);

    byte[] updateAdPhoto(Integer id, MultipartFile image) throws IOException;
}
