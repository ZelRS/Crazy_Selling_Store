package crazy_selling_store.repository;

import crazy_selling_store.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment getCommentByPk(Integer id);
}
