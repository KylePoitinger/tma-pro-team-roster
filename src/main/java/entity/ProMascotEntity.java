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

	@OneToOne(mappedBy = "mascot")
	public main.java.entity.ProTeamEntity team;

	public String description;

	public String costume;

	private String imageUrl;

	@Override
	public String toString() {
		return "ProMascotEntity{" +
				"mascotId=" + mascotId +
				", name='" + name + '\'' +
				", teamName='" + team + '\'' +
				", description='" + description + '\'' +
				", costume='" + costume + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				'}';
	}
}

