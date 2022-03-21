package sparta.team6.momo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Query("select distinct p from Plan p left join fetch p.user where p.user.email = :email")
    List<Plan> findAllByUserEmail(@Param("email") String email);

    @Query("select distinct p from Plan p left join p.user where p.user.email = :email")
    Page<Plan> findAllByUserEmail(@Param("email") String email, Pageable pageable);

//    Page<Plan> findAllByUserIdAndPlanDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("delete from Plan p where p.user.email = :email")
    List<Plan> deleteByUserEmail(@Param("email") String email);
}
