package crazy_selling_store.mapper;

import crazy_selling_store.dto.security.Register;
import crazy_selling_store.dto.users.User;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//маппер между DTO и сущностью пользователя
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //  происходит маппинг DTO Register в сущность UserEntity
    @Mapping(source = "username", target = "email")
    UserEntity toEntityUser(Register dto);

    //  происходит маппинг сущности UserEntity в DTO User
    User toDTOUser(UserEntity user);
}
