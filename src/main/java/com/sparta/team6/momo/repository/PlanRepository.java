package com.sparta.team6.momo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.team6.momo.model.Plan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findPlanByUrl(String url);

    List<Plan> findAllByAccountIdAndPlanDateBetween(Long accountId, LocalDateTime monthStart, LocalDateTime monthEnd);

    Page<Plan> findAllByAccountIdAndPlanDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Plan> findAllByAccountIdAndPlanNameContaining(Long accountId, String word, Pageable pageable);

    List<Plan> findAllByNoticeTimeBetween(LocalDateTime start, LocalDateTime end);
}
