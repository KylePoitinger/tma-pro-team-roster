package main.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.entity.ProPlayerEntity;

@Repository
public interface ProPlayerRepo extends JpaRepository<ProPlayerEntity, Long> {

	ProPlayerEntity getOneByPlayerId(long playerId);

}
