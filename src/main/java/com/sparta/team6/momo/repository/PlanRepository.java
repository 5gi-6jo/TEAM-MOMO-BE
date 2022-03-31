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

    List<Plan> findAllByUserIdAndPlanDateBetween(Long userId, LocalDateTime monthStart, LocalDateTime monthEnd);

    Page<Plan> findAllByUser_Id(Long userId, Pageable pageable);

    List<Plan> findAllByNoticeTimeBetween(LocalDateTime start, LocalDateTime end);
}
