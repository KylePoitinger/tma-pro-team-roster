package main.java.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class ProScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long scheduleId;

    public long playerId;

    public long arenaId;

    public String scheduledDate;

    public double ticketPrice;

    @Override
    public String toString() {
        return "ProScheduleEntity{" +
                "scheduleId=" + scheduleId +
                ", playerId=" + playerId +
                ", arenaId=" + arenaId +
                ", scheduledDate='" + scheduledDate + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
