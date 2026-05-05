---
name: tma-roster
description: Specialized context for the Pro Team Roster project (Spring Boot, Node.js, SQLite)
---

# Pro Team Roster Skill

Use this skill when working on the Pro Team Roster project, especially for backend (Java/Spring) and frontend (Node.js/Electron) tasks.

## Key Principles
- **Readability first**: This is legacy-style code intended for maintainability. Avoid overly complex abstractions.
- **Manual Boilerplate**: NO Lombok. Implement getters, setters, and `toString()` manually in entities.
- **SQLite Database**: The app uses SQLite (`pro_team_roster.db`). You can inspect it using SQLite tools if needed.

## Guidelines

### Backend (Java)
- Package structure: `main.java.*`.
- Use `@Autowired` for dependency injection.
- Entities must use `jakarta.persistence` (Spring Boot 3.3.0).
- Prevention of recursion: Use `@JsonManagedReference` on the parent side (e.g., `ProTeamEntity`) and `@JsonBackReference` on the child side (e.g., `ProPlayerEntity`).

### Testing
- **JaCoCo Coverage**: 
    - Services & Controllers: >80% Line, >70% Branch.
    - Run `mvn clean install` to verify.
- **Integration Tests**: Extend `BaseIntegrationTest` to reuse the Spring context and speed up execution.
- **Kafka**: Disabled by default in tests. Use `@EmbeddedKafka` if testing Kafka logic.

### Frontend (Node.js)
- **Manager Portal**: `src/main/nodejs/manager-portal`.
- **Admin Console**: `src/main/electron-launcher`.
- **ADA Compliance**: UI must follow WCAG AA standards (high contrast, alt text, focus states).

## Code Patterns

### Standard Service
```java
@Service
public class ProExampleService {
    @Autowired
    private ProExampleRepo exampleRepo;

    public List<ProExampleEntity> getAll() {
        return exampleRepo.findAll();
    }
}
```

### Standard Controller
```java
@RestController
@RequestMapping("/examples")
public class ProExampleController {
    @Autowired
    private ProExampleService exampleService;

    @GetMapping
    public List<ProExampleEntity> getExamples() {
        return exampleService.getAll();
    }
}
```
