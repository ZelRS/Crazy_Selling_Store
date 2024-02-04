package crazy_selling_store.repository;

import crazy_selling_store.entity.Ad;
import crazy_selling_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findByUser(User user);

    Ad getAdByPk(Integer id);
    // some code
}
