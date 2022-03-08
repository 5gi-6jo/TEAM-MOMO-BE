package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.User;

<<<<<<< HEAD
public interface UserRepository extends JpaRepository<Users, Long> {
=======
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
>>>>>>> 18b52db519838a44de6904e1d6ff40dd01ebc4e2
}
