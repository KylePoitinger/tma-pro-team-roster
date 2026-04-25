# GitHub Copilot Instructions – AI Coding Guide for tma-pro-team-roster
## Project Overview
Spring Boot REST API managing professional sports teams and player rosters. In-memory H2 database with a three-layer architecture (Controller → Service → Repository). Primary goal: readability and maintainability for legacy code scenarios.
**Build/Test:** Maven (pom.xml) | **Language:** Java 1.8 | **Framework:** Spring Boot 2.4.4
---
## Architecture & Data Flow
### Three-Layer Pattern
1. **Controllers** (`src/main/java/controller/`) – REST endpoints with request routing.
2. **Services** (`src/main/java/service/`) – Business logic and CRUD operations. All services are Spring `@Service` beans.
3. **Repositories** (`src/main/java/repository/`) – JPA interfaces extending `JpaRepository`.
### Data Models & Relationships
- **ProArenaEntity**: Top-level entity for venues.
- **ProTeamEntity**: Teams associated with an Arena (`@ManyToOne`). Has a list of Players (`@OneToMany`) and a Mascot (`@OneToOne`).
- **ProPlayerEntity**: Players linked to a Team (`@ManyToOne`).
- **ProMascotEntity**: Mascots linked to a Team (`@OneToOne`).
- **ProScheduleEntity**: Schedules linking a Team and an Arena (`@ManyToOne` for both).
- **Serialization:** Use `@JsonManagedReference` (in Team) and `@JsonBackReference` (in Player) to prevent infinite recursion in JSON responses.
---
## Critical Developer Workflows
### Build & Run
```bash
mvn clean install                    # Full build
mvn spring-boot:run                 # Start server (runs CommandLineRunner, seeds data)
mvn test                            # Run all tests
```
### Database
- **H2 in-memory** database; data resets on restart.
- **Console:** http://localhost:8080/h2-console (enabled in `application.properties`).
- **Initialization:** Automatic via `ProTeamRosterApplication.run()` (implements CommandLineRunner).
- **Scale:** Seeds 5 arenas, 8 teams, 11 players per team (88 total), 8 mascots, and 8 schedules on startup.
- **Timezone:** America/New_York (set in main method)
### Testing Strategy
- **Integration Tests:** Use `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`.
- **Key Tests:** 
    - `EndpointIntegrationTest`: Verifies health and core entity endpoints.
    - `ScaleDataIntegrationTest`: Verifies the requested scale of data (8 teams, 5 arenas, 11 players/team).
- **Verification:** Always run `mvn test -Dtest="main.java.integration.*Test"` after architectural changes.
---
## Package Structure & Naming
- `main.java.controller` – REST endpoint handlers  
- `main.java.service` – Business logic layer  
- `main.java.entity` – JPA-annotated domain objects with Lombok `@Data`  
- `main.java.repository` – JPA Repository interfaces
**Note:** The package structure prefix is `main.java.*`. Maintain this for consistency.
---
## Key Conventions & Gotchas
### Dependency Injection
Use standard Spring `@Autowired` for constructor or field injection. Services are NOT static.
### Repository Custom Queries
Repository methods use Spring's naming conventions + custom `@Query` annotations:
- `ProTeamRepo.getOneByTeamId()` – auto-derived.
- `deleteTeamById()` uses `@Modifying @Query`.
### REST Endpoint Patterns
- `GET /teams` – list all  
- `GET /teams/{teamId}/roster` – team + players
- `GET /schedules` – returns schedules with nested Team and Arena data.
- `GET /schedules/team/{teamId}` – filtered schedules.
### Entity Annotations
- Use Lombok `@Data` (generates getters, setters, toString, etc.).
- `@Entity` for JPA mapping.
- All relationships are now proper JPA mappings (no more string-based "teamName" references).
---
## External Dependencies & Versions
| Dependency | Version | Scope | Purpose |
|-----------|---------|-------|---------|
| Spring Boot | 2.4.4 | compile | Core framework + autoconfiguration |
| Lombok | 1.18.30 | provided | Boilerplate reduction (@Data) |
| H2 Database | (inherited) | runtime | In-memory database |
| JUnit 5 | (inherited) | test | Testing framework |
---
## Common Tasks
### Add New REST Endpoint
1. Add method to controller (`src/main/java/controller/`)
2. Implement logic in service (`src/main/java/service/`)
3. Update repository if custom query is needed.
4. Add verification to `EndpointIntegrationTest`.
### Modify Entity
1. Update field in entity class.
2. Update `ProTeamRosterApplication.run()` to seed the new field if necessary.
3. Update Service/Repository if logic depends on the field.
---
## Known Issues & Constraints
1. **H2 transient** – All data lost on restart; no persistence.
2. **Java 1.8** – Legacy version; avoid Java 9+ features.
3. **Testing** – Ensure tests are run with Maven to avoid environment-specific issues.
---
## AI Agent Hints
- **For changes:** Modify controller → service → repository (in order).
- **For data scale:** Check `ProTeamRosterApplication.java` for the current seeding logic.
- **For testing:** Leverage `TestRestTemplate` in integration tests.
- **For JSON:** Always check for circular references when adding new relationships.
