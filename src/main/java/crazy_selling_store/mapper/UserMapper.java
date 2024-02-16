package crazy_selling_store.mapper;

import crazy_selling_store.dto.security.Register;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Маппер между DTO и сущностью пользователя
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Маппинг DTO Register в сущность UserEntity
     */
    @Mapping(source = "username", target = "email")
    UserEntity toEntityUser(Register dto);

    /**
     * Маппинг сущности UserEntity в DTO User
     */
    User toDTOUser(UserEntity user);
}
