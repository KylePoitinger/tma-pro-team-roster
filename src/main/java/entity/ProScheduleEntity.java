package main.java.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ProScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long scheduleId;

    @ManyToOne
    @JoinColumn(name = "teamId")
    public ProTeamEntity team;

    @ManyToOne
    @JoinColumn(name = "arenaId")
    public ProArenaEntity arena;

    public String scheduledDate;

    public double ticketPrice;

    @Override
    public String toString() {
        return "ProScheduleEntity{" +
                "scheduleId=" + scheduleId +
                ", team=" + (team != null ? team.teamId : "null") +
                ", arena=" + (arena != null ? arena.arenaId : "null") +
                ", scheduledDate='" + scheduledDate + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
