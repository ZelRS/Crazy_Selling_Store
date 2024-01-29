package crazy_selling_store.mapper;

import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.Ad;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper( AdMapper.class);

    crazy_selling_store.dto.ads.Ad adEntityToAdDTO(Ad ad);

    Ads adToAds(Ad ad);

    CreateOrUpdateAd adToCreateAndUpdate(Ad ad);

    ExtendedAd adToExtendedAd(Ad ad);
    //----------------------------------------------------------------------------



    Ad adDTOToAdEntity(crazy_selling_store.dto.ads.Ad ad);


    Ad adsToAd(Ads ads);


    Ad CreateAndUpdateToAd(CreateOrUpdateAd createOrUpdateAd);


    Ad extendedAdToAd(ExtendedAd extendedAd);
}
