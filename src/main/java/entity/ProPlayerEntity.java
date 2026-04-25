package main.java.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ProPlayerEntity {

	@Id
	public long playerId;

	public String name;

	public String position;

	public String teamName;

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
				", teamName='" + teamName + '\'' +
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
