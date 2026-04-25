package main.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import main.java.entity.ProArenaEntity;

public interface ProArenaRepo extends JpaRepository<ProArenaEntity, Long> {

    ProArenaEntity getOneByArenaId(long arenaId);

    @Modifying
    @Transactional
    @Query("delete from ProArenaEntity a where a.arenaId = :arenaId")
    void deleteArenaById(@Param("arenaId") long arenaId);
}
