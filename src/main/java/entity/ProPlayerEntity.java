package main.java.entity;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class ProPlayerEntity {

	@Id
	public long playerId;

	public String name;

	public String position;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@com.fasterxml.jackson.annotation.JsonBackReference
	public ProTeamEntity team;

	public int age;

	public double height;

	public double weight;

	public String college;

	public double salary;

	public int jerseyNumber;

	public String nationality;

	public int contractYears;

	public String injuryStatus;

	public String stats;

	public String debutDate;

	@Override
	public String toString() {
		return "ProPlayerEntity{" +
				"playerId=" + playerId +
				", name='" + name + '\'' +
				", position='" + position + '\'' +
				", team=" + (team != null ? team.name : "null") +
				", age=" + age +
				", height=" + height +
				", weight=" + weight +
				", college='" + college + '\'' +
				", salary=" + salary +
				", jerseyNumber=" + jerseyNumber +
				", nationality='" + nationality + '\'' +
				", contractYears=" + contractYears +
				", injuryStatus='" + injuryStatus + '\'' +
				", stats='" + stats + '\'' +
				", debutDate='" + debutDate + '\'' +
				'}';
	}
}
