package main.java.entity;

import jakarta.persistence.*;

@Entity
public class ProMascotEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long mascotId;

	private String name;

	private String species;

	@OneToOne
	@JoinColumn(name = "team_id")
	private ProTeamEntity team;

	private String description;

	private String costume;

	private double height;

	private double weight;

	private String personality;

	private String firstAppearance;

	private String performerName;

	private String imageUrl;

	public long getMascotId() {
		return mascotId;
	}

	public void setMascotId(long mascotId) {
		this.mascotId = mascotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public ProTeamEntity getTeam() {
		return team;
	}

	public void setTeam(ProTeamEntity team) {
		this.team = team;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCostume() {
		return costume;
	}

	public void setCostume(String costume) {
		this.costume = costume;
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

	public String getPersonality() {
		return personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public String getFirstAppearance() {
		return firstAppearance;
	}

	public void setFirstAppearance(String firstAppearance) {
		this.firstAppearance = firstAppearance;
	}

	public String getPerformerName() {
		return performerName;
	}

	public void setPerformerName(String performerName) {
		this.performerName = performerName;
	}

	public String getImageUrl() {
		if (imageUrl == null || imageUrl.isEmpty()) {
			return "/images/random-mascot";
		}
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ProMascotEntity{" +
				"mascotId=" + mascotId +
				", name='" + name + '\'' +
				", species='" + species + '\'' +
				", description='" + description + '\'' +
				", costume='" + costume + '\'' +
				", height=" + height +
				", weight=" + weight +
				", personality='" + personality + '\'' +
				", firstAppearance='" + firstAppearance + '\'' +
				", performerName='" + performerName + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				'}';
	}
}
