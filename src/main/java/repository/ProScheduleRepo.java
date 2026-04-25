package main.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import main.java.entity.ProScheduleEntity;
import java.util.List;

@Repository
public interface ProScheduleRepo extends JpaRepository<ProScheduleEntity, Long> {
    List<ProScheduleEntity> findByPlayer_PlayerId(long playerId);
    List<ProScheduleEntity> findByArena_ArenaId(long arenaId);
}
