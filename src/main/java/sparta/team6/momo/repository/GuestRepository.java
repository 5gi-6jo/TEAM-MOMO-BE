package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
