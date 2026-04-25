package main.java.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class ProArenaEntity {

    @Id
    public long arenaId;

    public String name;

    public String location;

    public int capacity;

    public String teamName;

    @Override
    public String toString() {
        return "ProArenaEntity{" +
                "arenaId=" + arenaId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}