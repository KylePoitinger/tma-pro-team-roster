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

	public String teamName;

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
				", teamName='" + teamName + '\'' +
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
