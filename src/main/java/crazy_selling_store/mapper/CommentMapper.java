package crazy_selling_store.mapper;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//маппер между DTO и сущностью комментария
@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    //  происходит маппинг сущностей UserEntity и CommentEntity в DTO Comment
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.image", target = "authorImage")
    @Mapping(source = "user.id", target = "author")
    Comment toDTOComment(UserEntity user, CommentEntity comment);
}
