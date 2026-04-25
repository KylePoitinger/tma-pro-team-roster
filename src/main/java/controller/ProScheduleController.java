package main.java.controller;

import main.java.entity.ProScheduleEntity;
import main.java.service.ProScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@Tag(name = "Pro Schedule Controller", description = "Endpoints for managing game schedules and ticket prices")
public class ProScheduleController {

    @Autowired
    private ProScheduleService proScheduleService;

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Returns a list of all game schedules including home and away teams and arena details")
    public List<ProScheduleEntity> getAllSchedules() {
        return proScheduleService.getAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public ProScheduleEntity getSchedule(@PathVariable long scheduleId) {
        return proScheduleService.getSchedule(scheduleId);
    }

    @GetMapping("/team/{teamId}")
    public List<ProScheduleEntity> getSchedulesByTeam(@PathVariable long teamId) {
        return proScheduleService.getSchedulesByTeam(teamId);
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
