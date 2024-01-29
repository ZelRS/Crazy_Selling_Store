package crazy_selling_store.mapper;

import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import crazy_selling_store.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class);

    crazy_selling_store.dto.comments.Comment commentEntityToCommentDTO(Comment comment);

    Comments commentToComments(Comment comment);


    CreateOrUpdateComment commentToCreateAndUpdateComment(Comment comment);
    //-----------------------------------------------------------------------------------------



    Comment commentDTOToCommentEntity(crazy_selling_store.dto.comments.Comment comment);

    Comment commentsToComment(Comments comments);

    Comment CreateOrUpdateCommentToComment(CreateOrUpdateComment createOrUpdateComment);


}
