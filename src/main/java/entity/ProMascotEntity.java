package main.java.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ProMascotEntity {

	@Id
	public long mascotId;

	public String name;

	public String teamName;

	public String description;

	public String costume;

}

