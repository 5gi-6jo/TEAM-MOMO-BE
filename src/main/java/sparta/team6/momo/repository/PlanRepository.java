package sparta.team6.momo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sparta.team6.momo.model.Plan;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Override
    Page<Plan> findAll(Pageable pageable);

    @Query("select distinct p from Plan p left join fetch p.user where p.user.email = :email")
    List<Plan> findAllByUserEmail(@Param("email") String email);
}
