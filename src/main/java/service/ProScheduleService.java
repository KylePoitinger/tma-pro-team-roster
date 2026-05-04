package main.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.entity.ProScheduleEntity;
import main.java.exception.ResourceNotFoundException;
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
        return proScheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for this id :: " + scheduleId));
    }

    public List<ProScheduleEntity> getSchedulesByTeam(long teamId) {
        List<ProScheduleEntity> homeSchedules = proScheduleRepo.findByHomeTeam_TeamId(teamId);
        List<ProScheduleEntity> awaySchedules = proScheduleRepo.findByAwayTeam_TeamId(teamId);
        homeSchedules.addAll(awaySchedules);
        return homeSchedules;
    }

    public List<ProScheduleEntity> getSchedulesByArena(long arenaId) {
        return proScheduleRepo.findByArena_ArenaId(arenaId);
    }

    public ProScheduleEntity createSchedule(ProScheduleEntity schedule) {
        return proScheduleRepo.save(schedule);
    }

    public ProScheduleEntity updateSchedule(long scheduleId, ProScheduleEntity updateReq) {
        return proScheduleRepo.findById(scheduleId).map(schedule -> {
            schedule.setHomeTeam(updateReq.getHomeTeam());
            schedule.setAwayTeam(updateReq.getAwayTeam());
            schedule.setArena(updateReq.getArena());
            schedule.setScheduledDate(updateReq.getScheduledDate());
            schedule.setTicketPrice(updateReq.getTicketPrice());
            return proScheduleRepo.save(schedule);
        }).orElseThrow(() -> new ResourceNotFoundException("Schedule not found for this id :: " + scheduleId));
    }

    public String deleteSchedule(long scheduleId) {
        ProScheduleEntity schedule = proScheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for this id :: " + scheduleId));

        proScheduleRepo.delete(schedule);
        return "Delete was successful for schedule:" + scheduleId;
    }
}
