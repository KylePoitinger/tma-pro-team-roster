package main.java.entity;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class ProMascotEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long mascotId;

	public String name;

	public String species;

	@OneToOne
	@JoinColumn(name = "team_id")
	public ProTeamEntity team;

	public String description;

	public String costume;

	public double height;

	public double weight;

	public String personality;

	public String firstAppearance;

	public String performerName;

	private String imageUrl;

	@Override
	public String toString() {
		return "ProMascotEntity{" +
				"mascotId=" + mascotId +
				", name='" + name + '\'' +
				", species='" + species + '\'' +
				", team=" + (team != null ? team.name : "null") +
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
