package crazy_selling_store.mapper;

import crazy_selling_store.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntityUser(crazy_selling_store.dto.users.UpdateUser dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.users.UpdateUser toDtoUpdateUser(User user);

    User toEntityUser(crazy_selling_store.dto.users.User dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.users.User toDtoUser(User user);
}
