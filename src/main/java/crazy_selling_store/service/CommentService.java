package crazy_selling_store.service;

import crazy_selling_store.dto.comments.Comment;
import crazy_selling_store.dto.comments.Comments;
import crazy_selling_store.dto.comments.CreateOrUpdateComment;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {
    Comments getAdComments(Integer id);

    @Transactional
    Comment createAdComment(Integer id, CreateOrUpdateComment text, Authentication authentication);

    @Transactional
    boolean deleteAdComment(Integer adId, Integer commentId);

    @Transactional
     Comment updateAdComment(Integer adId, Integer commentId, CreateOrUpdateComment text);
}
