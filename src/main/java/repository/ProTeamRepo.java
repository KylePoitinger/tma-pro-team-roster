package main.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import main.java.entity.ProTeamEntity;

@Repository
public interface ProTeamRepo extends JpaRepository<ProTeamEntity, Long> {

	List<ProTeamEntity> findAll();

	ProTeamEntity getOneByTeamId(long teamId);

	List<ProTeamEntity> getTeamsByName(String name);

	List<ProTeamEntity> getTeamsByCity(String city);

	List<ProTeamEntity> getTeamsByMascot(String mascot);

	@Modifying
	@Query("delete from ProTeamEntity b where b.teamId=:teamId")
	void deleteTeamById(@Param("teamId") long teamId);
}
