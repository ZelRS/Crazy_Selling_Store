package crazy_selling_store.mapper;

import crazy_selling_store.dto.security.Login;
import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.security.Register;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    //-------------------to Entity mappers------------------------------
//    User toEntityUser(crazy_selling_store.dto.users.User dto); данный маппинг возможно не понадобится


    UserEntity toEntityUser(UpdateUser dto);


    @Mapping(source = "username", target = "email")
    UserEntity toEntityUser(Login dto);

    @Mapping(source = "currentPassword", target = "password")
    UserEntity toEntityUser(NewPassword dto);

    @Mapping(source = "username", target = "email")
    UserEntity toEntityUser(Register dto);


    //----------------------to DTO mappers--------------------------------
    crazy_selling_store.dto.users.User toDTOUser(UserEntity user);

    UpdateUser toDTOUpdateUser(UserEntity user);

    @Mapping(source = "email", target = "username")
    Login toDTOLogin(UserEntity user);

    @Mapping(source = "password", target = "newPassword")
    NewPassword toDTONewPassword(UserEntity user);

    @Mapping(source = "email", target = "username")
    Register toDTORegister(UserEntity user);
}
