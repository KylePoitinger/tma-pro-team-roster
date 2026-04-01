package main.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import main.java.entity.ProMascotEntity;

@Repository
public interface ProMascotRepo extends JpaRepository<ProMascotEntity, Long> {

	ProMascotEntity getOneByMascotId(long mascotId);

	List<ProMascotEntity> getMascotsByTeamName(String teamName);

	List<ProMascotEntity> getMascotsByName(String name);

	@Modifying
	@Transactional
	@Query("delete from ProMascotEntity m where m.mascotId=:mascotId")
	void deleteMascotById(@Param("mascotId") long mascotId);

}

