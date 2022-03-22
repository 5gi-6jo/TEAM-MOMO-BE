package sparta.team6.momo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByUserIdAndPlanDateBetween(Long userId, LocalDateTime monthStart, LocalDateTime monthEnd);

    Page<Plan> findAllByUserIdAndPlanDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Plan> findAllByUserIdAndPlanNameContaining(Long userId, String word, Pageable pageable);
}
