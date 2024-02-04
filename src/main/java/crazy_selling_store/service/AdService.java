package crazy_selling_store.service;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdService {
    Ad createAd(CreateOrUpdateAd properties, MultipartFile image, Authentication authentication) throws IOException;

    Ads getAllAds();

    Ads getAuthUserAds(Authentication authentication);


}
