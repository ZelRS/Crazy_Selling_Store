package crazy_selling_store.mapper;

import crazy_selling_store.dto.ads.Ads;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.Ad;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdMapper {

    Ad toEntityAd(crazy_selling_store.dto.ads.Ad dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.ads.Ad toDtoAd(Ad ad);

    Ad toEntityAd(Ads dto);
    @InheritInverseConfiguration
    Ads toDtoAds(Ad ad);

    Ad toEntityAd(CreateOrUpdateAd dto);
    @InheritInverseConfiguration
    CreateOrUpdateAd toDtoCreateOrUpdateAd(Ad ad);

    Ad toEntityAd(ExtendedAd dto);
    @InheritInverseConfiguration
    ExtendedAd toDtoExtendedAd(Ad ad);
}
