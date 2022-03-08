package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
