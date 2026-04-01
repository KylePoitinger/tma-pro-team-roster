package main.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.entity.ProPlayerEntity;

@Repository
public interface ProPlayerRepo extends JpaRepository<ProPlayerEntity, Long> {

	ProPlayerEntity getOneByPlayerId(long playerId);

	@Modifying
	@Transactional
	@Query("delete from ProPlayerEntity p where p.playerId=:playerId")
	void deletePlayerById(@Param("playerId") long playerId);

}
