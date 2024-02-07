package crazy_selling_store.repository;

import crazy_selling_store.entity.AdEntity;
import crazy_selling_store.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    List<AdEntity> findByUser(UserEntity user);

    AdEntity getAdByPk(Integer id);

    void deleteByPk(Integer id);
}
