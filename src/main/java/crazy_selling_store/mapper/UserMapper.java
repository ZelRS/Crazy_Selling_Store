package crazy_selling_store.mapper;

import crazy_selling_store.dto.security.Login;
import crazy_selling_store.dto.security.NewPassword;
import crazy_selling_store.dto.security.Register;
import crazy_selling_store.dto.users.UpdateUser;
import crazy_selling_store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    //-------------------to Entity mappers------------------------------
//    User toEntityUser(crazy_selling_store.dto.users.User dto); данный маппинг возможно не понадобится


    User toEntityUser(UpdateUser dto);


    @Mapping(source = "username", target = "email")
    User toEntityUser(Login dto);

    @Mapping(source = "currentPassword", target = "password")
    User toEntityUser(NewPassword dto);

    @Mapping(source = "username", target = "email")
    User toEntityUser(Register dto);


    //----------------------to DTO mappers--------------------------------
    crazy_selling_store.dto.users.User toDTOUser(User user);

    UpdateUser toDTOUpdateUser(User user);

    @Mapping(source = "email", target = "username")
    Login toDTOLogin(User user);

    @Mapping(source = "password", target = "newPassword")
    NewPassword toDTONewPassword(User user);

    @Mapping(source = "email", target = "username")
    Register toDTORegister(User user);
}
