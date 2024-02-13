package crazy_selling_store.repository;

import crazy_selling_store.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    CommentEntity getCommentByPk(Integer id);
    Optional<CommentEntity> findByPk(Integer pk);
}
