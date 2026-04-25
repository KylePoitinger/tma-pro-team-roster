package main.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.java.entity.ProScheduleEntity;
import main.java.repository.ProScheduleRepo;
import java.util.List;

@Service
public class ProScheduleService {

    @Autowired
    private ProScheduleRepo proScheduleRepo;

    public List<ProScheduleEntity> getAllSchedules() {
        return proScheduleRepo.findAll();
    }

    public ProScheduleEntity getSchedule(long scheduleId) {
        return proScheduleRepo.findById(scheduleId).orElse(null);
    }

    public List<ProScheduleEntity> getSchedulesByTeam(long teamId) {
        return proScheduleRepo.findByTeam_TeamId(teamId);
    }

    public List<ProScheduleEntity> getSchedulesByArena(long arenaId) {
        return proScheduleRepo.findByArena_ArenaId(arenaId);
    }

    public ProScheduleEntity createSchedule(ProScheduleEntity schedule) {
        return proScheduleRepo.save(schedule);
    }

    public ProScheduleEntity updateSchedule(long scheduleId, ProScheduleEntity updateReq) {
        return proScheduleRepo.findById(scheduleId).map(schedule -> {
            schedule.team = updateReq.team;
            schedule.arena = updateReq.arena;
            schedule.scheduledDate = updateReq.scheduledDate;
            schedule.ticketPrice = updateReq.ticketPrice;
            return proScheduleRepo.save(schedule);
        }).orElseGet(() -> {
            updateReq.scheduleId = scheduleId;
            return proScheduleRepo.save(updateReq);
        });
    }

    public String deleteSchedule(long scheduleId) {
        try {
            proScheduleRepo.deleteById(scheduleId);
            return "Delete was successful for schedule:" + scheduleId;
        } catch (Exception e) {
            return "Delete was unsuccessful with error: " + e.toString();
        }
    }
}
