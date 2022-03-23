package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Account;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(String id);
}
