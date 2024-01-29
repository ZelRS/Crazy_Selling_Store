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


    crazy_selling_store.dto.users.User userEntityToUserDTO(User user);

    UpdateUser userToUpdateUser(User user);

    @Mapping(source = "password", target = "currentPassword")
    NewPassword userToNewPassword(User user);

    @Mapping(source = "email", target = "username")
    Register userToRegister(User user);

    @Mapping(source = "email", target = "username")
    Login userToLogin(User user);
    //-------------------------------------------------------------------------



    User userDTOToUserEntity(crazy_selling_store.dto.users.User user);

    @Mapping(source = "newPassword", target = "password")
    User newPasswordToUser(NewPassword newPassword);

    User updateUserToUser(UpdateUser updateUser);

    @Mapping(source = "username", target = "email")
    User registerToUser(Register register);

    @Mapping(source = "username", target = "email")
    User loginToUser(Login login);
}
