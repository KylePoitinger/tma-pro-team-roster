package main.java.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class ProTeamEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long teamId;

	private String name;

	private String city;

	private String mascot;

	private int foundedYear;

	@ManyToOne
	@JoinColumn(name = "arena_id")
	private ProArenaEntity arena;

	private int championships;

	private String owner;

	private String colors;

	private String website;

	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
	@com.fasterxml.jackson.annotation.JsonManagedReference
	private List<ProPlayerEntity> proPlayers;

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMascot() {
		return mascot;
	}

	public void setMascot(String mascot) {
		this.mascot = mascot;
	}

	public int getFoundedYear() {
		return foundedYear;
	}

	public void setFoundedYear(int foundedYear) {
		this.foundedYear = foundedYear;
	}

	public ProArenaEntity getArena() {
		return arena;
	}

	public void setArena(ProArenaEntity arena) {
		this.arena = arena;
	}

	public int getChampionships() {
		return championships;
	}

	public void setChampionships(int championships) {
		this.championships = championships;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<ProPlayerEntity> getProPlayers() {
		return proPlayers;
	}

	public void setProPlayers(List<ProPlayerEntity> proPlayers) {
		this.proPlayers = proPlayers;
	}

	@Override
	public String toString() {
		return "ProTeamEntity{" +
				"teamId=" + teamId +
				", name='" + name + '\'' +
				", city='" + city + '\'' +
				", mascot='" + mascot + '\'' +
				", foundedYear=" + foundedYear +
				", arena=" + (arena != null ? arena.getName() : "null") +
				", championships=" + championships +
				", owner='" + owner + '\'' +
				", colors='" + colors + '\'' +
				", website='" + website + '\'' +
				", proPlayers=" + proPlayers +
				'}';
	}
}
