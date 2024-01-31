package crazy_selling_store.repository;

import crazy_selling_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // some code

    Optional<User> findUserByEmail(String email);
}
