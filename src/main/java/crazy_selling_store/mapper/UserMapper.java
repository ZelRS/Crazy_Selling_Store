package crazy_selling_store.mapper;

import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toEntityUser(crazy_selling_store.dto.users.UpdateUser dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.users.UpdateUser toDtoUpdateUser(User user);

    User toEntityUser(crazy_selling_store.dto.users.User dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.users.User toDtoUser(User user);
    @Mapping(source = "username", target = "email")
    User toEntityUser(crazy_selling_store.dto.security.Login dto);
    @InheritInverseConfiguration
    @Mapping(source = "email", target = "username")
    crazy_selling_store.dto.security.Login toDtoLogin(User user);
    @Mapping(source = "currentPassword", target = "password")
    User toEntityUser(crazy_selling_store.dto.security.NewPassword dto);
    @InheritInverseConfiguration
    @Mapping(source = "password", target = "newPassword")
    crazy_selling_store.dto.security.NewPassword toDtoNewPassword(User user);
    @Mapping(source = "username", target = "email")
    User toEntityUser(crazy_selling_store.dto.security.Register dto);
    @InheritInverseConfiguration
    @Mapping(source = "email", target = "username")
    crazy_selling_store.dto.security.Register toDtoRegister(User user);
}
