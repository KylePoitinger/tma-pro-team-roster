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

	public String stadium;

	public int championships;

	public String owner;

	public String colors;

	public String website;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "teamName", referencedColumnName = "name")
	public List<main.java.entity.ProPlayerEntity> proPlayers;

	@Override
	public String toString() {
		return "ProTeamEntity{" +
				"teamId=" + teamId +
				", name='" + name + '\'' +
				", city='" + city + '\'' +
				", mascot='" + mascot + '\'' +
				", foundedYear=" + foundedYear +
				", stadium='" + stadium + '\'' +
				", championships=" + championships +
				", owner='" + owner + '\'' +
				", colors='" + colors + '\'' +
				", website='" + website + '\'' +
				", proPlayers=" + proPlayers +
				'}';
	}
}
