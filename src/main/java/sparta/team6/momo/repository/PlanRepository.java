package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Plans;

public interface PlanRepository extends JpaRepository<Plans, Long> {
}
