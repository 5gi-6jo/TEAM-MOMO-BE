package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Destination findByPlanId(Long planId);
}
