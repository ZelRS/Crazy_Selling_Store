package crazy_selling_store.mapper;

import crazy_selling_store.dto.ads.Ad;
import crazy_selling_store.dto.ads.CreateOrUpdateAd;
import crazy_selling_store.dto.ads.ExtendedAd;
import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер между DTO и сущностью объявления
 */
@Mapper(componentModel = "spring")
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper(AdMapper.class);

    /**
     * Маппинг @param DTO CreateOrUpdate в сущность @return AdEntity
     */
    AdEntity toEntityAd(CreateOrUpdateAd dto);

    /**
     * Маппинг сущностей UserEntity и AdEntity в DTO Ad
     */
    @Mapping(source = "user.id", target = "author")
    @Mapping(source = "ad.image", target = "image")
    Ad toDTOAd(UserEntity user, AdEntity ad);

    /**
     * Маппинг сущностей UserEntity и AdEntity в DTO  ExtendedAd
     */
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.lastName", target = "authorLastName")
    @Mapping(source = "ad.image", target = "image")
    ExtendedAd toDTOExtendedAd(UserEntity user, AdEntity ad);
}
