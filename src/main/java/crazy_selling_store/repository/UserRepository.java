package crazy_selling_store.repository;

import crazy_selling_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // some code
}
