package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
}
