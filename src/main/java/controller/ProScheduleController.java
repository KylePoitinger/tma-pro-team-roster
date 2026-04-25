package main.java.controller;

import main.java.entity.ProScheduleEntity;
import main.java.service.ProScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ProScheduleController {

    @Autowired
    private ProScheduleService proScheduleService;

    @GetMapping
    public List<ProScheduleEntity> getAllSchedules() {
        return proScheduleService.getAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public ProScheduleEntity getSchedule(@PathVariable long scheduleId) {
        return proScheduleService.getSchedule(scheduleId);
    }

    @GetMapping("/player/{playerId}")
    public List<ProScheduleEntity> getSchedulesByPlayer(@PathVariable long playerId) {
        return proScheduleService.getSchedulesByPlayer(playerId);
    }

    @GetMapping("/arena/{arenaId}")
    public List<ProScheduleEntity> getSchedulesByArena(@PathVariable long arenaId) {
        return proScheduleService.getSchedulesByArena(arenaId);
    }

    @PostMapping
    public ProScheduleEntity createSchedule(@RequestBody ProScheduleEntity schedule) {
        return proScheduleService.createSchedule(schedule);
    }

    @PutMapping("/{scheduleId}")
    public ProScheduleEntity updateSchedule(@PathVariable long scheduleId, @RequestBody ProScheduleEntity schedule) {
        return proScheduleService.updateSchedule(scheduleId, schedule);
    }

    @DeleteMapping("/{scheduleId}")
    public String deleteSchedule(@PathVariable long scheduleId) {
        return proScheduleService.deleteSchedule(scheduleId);
    }
}
