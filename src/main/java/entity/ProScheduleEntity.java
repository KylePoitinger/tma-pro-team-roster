package main.java.entity;

import jakarta.persistence.*;

@Entity
public class ProScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long scheduleId;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private ProTeamEntity homeTeam;

    @ManyToOne
    @JoinColumn(name = "awayTeamId")
    private ProTeamEntity awayTeam;

    @ManyToOne
    @JoinColumn(name = "arenaId")
    private ProArenaEntity arena;

    private String scheduledDate;

    private double ticketPrice;

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ProTeamEntity getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(ProTeamEntity homeTeam) {
        this.homeTeam = homeTeam;
    }

    public ProTeamEntity getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(ProTeamEntity awayTeam) {
        this.awayTeam = awayTeam;
    }

    public ProArenaEntity getArena() {
        return arena;
    }

    public void setArena(ProArenaEntity arena) {
        this.arena = arena;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public String toString() {
        return "ProScheduleEntity{" +
                "scheduleId=" + scheduleId +
                ", scheduledDate='" + scheduledDate + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
