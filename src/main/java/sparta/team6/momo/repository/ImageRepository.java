package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPlan_Id(Long id);
}
