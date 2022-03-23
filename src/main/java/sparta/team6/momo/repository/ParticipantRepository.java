package sparta.team6.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.team6.momo.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
