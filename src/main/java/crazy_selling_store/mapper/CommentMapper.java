package crazy_selling_store.mapper;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.entity.CommentEntity;
import crazy_selling_store.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);



    //-------------------to Entity mappers------------------------------
//  Comment toEntityComment(crazy_selling_store.dto.comments.Comment dto); данный маппинг возможно не понадобится
//  Comment toEntityComment(Comments dto); данный маппинг возможно не понадобится


    //  происходит маппинг DTO с единственным полем text в аналогичное поле сущности объявления Comment
    CommentEntity toEntityComment(CreateOrUpdateComment dto);






    //----------------------to DTO mappers--------------------------------
//  CreateOrUpdateComment toDTOCreateOrUpdateComment(Comment comment); данный маппинг возможно не понадобится
//  Comments toDTOComments(Comment comment); данный маппинг возможно не понадобится



    //  происходит маппинг необходимых полей из сущности User и Comment в DTO комментария Comment
    @Mapping(source = "user.firstName", target = "authorFirstName")
    @Mapping(source = "user.image", target = "authorImage")
    @Mapping(source = "user.id", target = "author")
    Comment toDTOComment(UserEntity user, CommentEntity comment);
}
