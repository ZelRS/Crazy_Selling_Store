package crazy_selling_store.repository;

import crazy_selling_store.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Integer> {
    // some code
}
