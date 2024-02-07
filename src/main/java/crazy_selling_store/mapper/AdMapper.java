package crazy_selling_store.mapper;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);


    //-------------------to Entity mappers------------------------------
//  Ad toEntityAd(crazy_selling_store.dto.ads.Ad dto); данный маппинг возможно не понадобится
//  Ad toEntityAd(ExtendedAd dto); данный маппинг возможно не понадобится
//  Ad toEntityAd(Ads dto); данный маппинг возможно не понадобится


    //  происходит маппинг DTO с единственным полем text в аналогичное поле сущности объявления Ad
    AdEntity toEntityAd(CreateOrUpdateAd dto);






    //----------------------to DTO mappers--------------------------------

//  CreateOrUpdateAd toDTOCreateOrUpdateAd(Ad ad); данный маппинг возможно не понадобится
//  Ads toDTOAds(Ad ad); данный маппинг возможно не понадобится


    //  происходит маппинг необходимых полей из сущности User и Ad в DTO объявления Ad

    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "ad.image", target = "image")
    Ad toDTOAd(UserEntity user, AdEntity ad);


    //  происходит маппинг необходимых полей из сущности User и Ad в DTO расширенного объявления ExtendedAd
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "ad.image", target = "image")
    ExtendedAd toDTOExtendedAd(UserEntity user, AdEntity ad);
}
