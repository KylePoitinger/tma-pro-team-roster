package main.java.entity;

import jakarta.persistence.*;

@Entity
public class ProPlayerEntity {

	@Id
	private long playerId;

	private String name;

	private String position;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@com.fasterxml.jackson.annotation.JsonBackReference
	private ProTeamEntity team;

	private int age;

	private double height;

	private double weight;

	private String college;

	private double salary;

	private int jerseyNumber;

	private String nationality;

	private int contractYears;

	private String injuryStatus;

	private String stats;

	private String debutDate;

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public ProTeamEntity getTeam() {
		return team;
	}

	public void setTeam(ProTeamEntity team) {
		this.team = team;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getJerseyNumber() {
		return jerseyNumber;
	}

	public void setJerseyNumber(int jerseyNumber) {
		this.jerseyNumber = jerseyNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public int getContractYears() {
		return contractYears;
	}

	public void setContractYears(int contractYears) {
		this.contractYears = contractYears;
	}

	public String getInjuryStatus() {
		return injuryStatus;
	}

	public void setInjuryStatus(String injuryStatus) {
		this.injuryStatus = injuryStatus;
	}

	public String getStats() {
		return stats;
	}

	public void setStats(String stats) {
		this.stats = stats;
	}

	public String getDebutDate() {
		return debutDate;
	}

	public void setDebutDate(String debutDate) {
		this.debutDate = debutDate;
	}

	@Override
	public String toString() {
		return "ProPlayerEntity{" +
				"playerId=" + playerId +
				", name='" + name + '\'' +
				", position='" + position + '\'' +
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
