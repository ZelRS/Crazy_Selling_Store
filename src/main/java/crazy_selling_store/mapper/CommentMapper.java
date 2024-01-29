package crazy_selling_store.mapper;

import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.entity.Ad;
import crazy_selling_store.entity.Comment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toEntityComment(crazy_selling_store.dto.comments.Comment dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.comments.Comment toDtoComment(Comment comment);

    Comment toEntityComment(crazy_selling_store.dto.comments.Comments dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.comments.Comments toDtoComments(Comment comment);

    Comment toEntityComment(crazy_selling_store.dto.comments.CreateOrUpdateComment dto);
    @InheritInverseConfiguration
    crazy_selling_store.dto.comments.CreateOrUpdateComment toDtoCreateOrUpdateComment(Comment comment);


}
