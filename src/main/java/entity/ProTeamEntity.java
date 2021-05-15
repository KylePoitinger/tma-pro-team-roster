package main.java.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class ProTeamEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long teamId;

	public String name;

	public String city;

	public String mascot;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "teamName", referencedColumnName = "name")
	public List<ProPlayerEntity> proPlayers;

}
