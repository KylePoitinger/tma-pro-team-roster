package main.java.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

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

	public int foundedYear;

	@ManyToOne
	@JoinColumn(name = "arena_id")
	public ProArenaEntity arena;

	public int championships;

	public String owner;

	public String colors;

	public String website;

	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
	@com.fasterxml.jackson.annotation.JsonManagedReference
	public List<ProPlayerEntity> proPlayers;

	@Override
	public String toString() {
		return "ProTeamEntity{" +
				"teamId=" + teamId +
				", name='" + name + '\'' +
				", city='" + city + '\'' +
				", mascot='" + mascot + '\'' +
				", foundedYear=" + foundedYear +
				", arena=" + (arena != null ? arena.name : "null") +
				", championships=" + championships +
				", owner='" + owner + '\'' +
				", colors='" + colors + '\'' +
				", website='" + website + '\'' +
				", proPlayers=" + proPlayers +
				'}';
	}
}
