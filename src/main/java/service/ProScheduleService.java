package main.java.service;

import java.util.Optional;

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
        }).orElseThrow(() -> new ResourceNotFoundException("Schedule not found for this id :: " + scheduleId));
    }

    public String deleteSchedule(long scheduleId) {
        ProScheduleEntity schedule = proScheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for this id :: " + scheduleId));

        proScheduleRepo.delete(schedule);
        return "Delete was successful for schedule:" + scheduleId;
    }
}
