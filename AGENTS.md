# AGENTS.md – AI Coding Guide for tma-pro-team-roster

## Project Overview
Spring Boot REST API managing professional sports teams and player rosters. In-memory H2 database with a three-layer architecture (Controller → Service → Repository). Primary goal: readability and maintainability for legacy code scenarios.

**Build/Test:** Maven (pom.xml) | **Language:** Java 1.8 | **Framework:** Spring Boot 2.4.4

---

## Architecture & Data Flow

### Three-Layer Pattern
1. **Controllers** (`src/main/java/controller/`) – REST endpoints with request routing
2. **Services** (`src/main/java/service/`) – Business logic, CRUD operations (all static methods)
3. **Repositories** (`src/main/java/repository/`) – JPA interfaces with custom query methods

**Key Design Choice:** Services use static methods with autowired static repository fields (see `ProTeamService.java:14-18`). Avoid instance methods or dependency injection changes without understanding the intentional trade-off.

### Data Models
- **ProTeamEntity**: Teams with roster relationship (`teamId` PK, name, city, mascot, `List<ProPlayerEntity>` via OneToMany LAZY fetch)
- **ProPlayerEntity**: Players linked to teams by `teamName` string reference (not FK), contains salary, college, physical attributes
- **Relationship:** Team.proPlayers joined on `(teamName, name)` columns—unusual pattern, preserve when modifying

---

## Critical Developer Workflows

### Build & Run
```bash
mvn clean install                    # Full build
mvn spring-boot:run                 # Start server (runs CommandLineRunner, seeds data)
mvn test                            # Run tests (limited due to wiring issues; see Testing)
```

### Database
- **H2 in-memory** database; data resets on restart
- **Console:** http://localhost:8080/h2-console (enabled in `application.properties`)
- **Initialization:** Automatic via `ProTeamRosterApplication.run()` (implements CommandLineRunner)
- **Timezone:** America/New_York (set in main method)

### Testing Context
Tests are **not properly wired** (see README.md:12-18). Current test files lack Spring context; direct service testing without mocks. Avoid relying on test infrastructure for new features until dependencies are fixed. Use manual testing via REST endpoints instead.

---

## Package Structure & Naming

- `main.java.controller` – REST endpoint handlers  
- `main.java.service` – Static business logic wrappers around repositories  
- `main.java.entity` – JPA-annotated domain objects with Lombok `@Data`  
- `main.java.repository` – JPA Repository interfaces extending `JpaRepository<T, Long>`

**Unusual Pattern:** Non-standard package names use `main.java.*` instead of domain-based naming (e.g., `com.company.project.*`). Maintain this structure for consistency.

---

## Key Conventions & Gotchas

### Static Service Methods
All service methods are static (e.g., `ProTeamService.getTeams()`, `ProPlayerService.updateProPlayer()`). This is atypical but intentional.
- Don't refactor to instance methods without explicit request
- Constructor assigns static field: `ProTeamService.proTeamRepo = proTeamRepo`
- Call via class name: `ProTeamService.createTeam(team)`

### Repository Custom Queries
Repository methods use Spring's naming conventions + custom `@Query` annotations:
- `ProTeamRepo.getOneByTeamId()`, `getTeamsByName()`, `getTeamsByCity()` – auto-derived
- `deleteTeamById()` uses `@Modifying @Query("delete from ProTeamEntity b where b.teamId=:teamId")`
- **ProPlayerRepo**: minimal custom methods; relies on inheritance

### REST Endpoint Patterns
- `GET /teams` – list all  
- `GET /teams/{teamId}/roster` – team + players (eager load proPlayers)  
- `GET /teams/fields?team-name=X&team-city=Y&team-mascot=Z` – lookup by field  
- `POST /teams` – create  
- `PUT /teams/{teamId}` – update (upsert if not found)  
- `DELETE /teams/{teamId}` – delete  
- Similar pattern for `/players/{playerId}` (no list endpoint)

### Entity Annotations
- Use Lombok `@Data` (generates getters, setters, toString, equals, hashCode)
- `@Entity` for JPA mapping  
- `@Id` for primary key (non-standard types: `long` with `public` access)
- `@OneToMany` relationships use LAZY fetch; avoid unnecessary roster loading

### String-Based Team Relationship
Players reference teams via `teamName` string field, not foreign key. Updates to team names may orphan player records—be aware when renaming teams.

---

## External Dependencies & Versions

| Dependency | Version | Scope | Purpose |
|-----------|---------|-------|---------|
| Spring Boot | 2.4.4 | compile | Core framework + autoconfiguration |
| Spring Data JPA | (included) | compile | Repository abstraction, H2 integration |
| Lombok | 1.18.30 | provided | Boilerplate reduction (@Data, @Autowired fields) |
| H2 Database | (inherited) | runtime | In-memory database |
| JUnit 5 | 5.10.0 | test | Testing framework (partially wired) |
| JUnit 4 | (inherited) | test | Legacy test support (junit-jupiter-engine) |

**No external REST clients, DTOs, or validation libraries.** Add cautiously if needed.

---

## Common Tasks

### Add New REST Endpoint
1. Add method to controller (`src/main/java/controller/ProPlayerController.java`)
2. Implement logic in service (`src/main/java/service/ProPlayerService.java`) as static method
3. Repository auto-derives query or add custom `@Query` method
4. Test via `mvn spring-boot:run` and curl/Postman

### Modify Entity
1. Update field in entity class (e.g., `ProTeamEntity.java`)
2. H2 auto-migrates schema on restart
3. Update relevant service methods if new field added
4. Check repository queries; may need new custom methods

### Add Logging
Use SLF4J (already configured): `Logger LOG = LoggerFactory.getLogger("ClassName");`  
Follow application startup pattern in `ProTeamRosterApplication.java:24`

---

## Testing Strategy
- **Current:** Manual REST testing via HTTP client (application boots with seed data)
- **Future:** Fix JUnit wiring (investigate MockMvc, @SpringBootTest, @DataJpaTest)
- **Avoid:** Complex mocking frameworks until test infrastructure is stable

---

## Known Issues & Constraints

1. **Static services** – Unusual for Spring; makes dependency injection less flexible
2. **Test infrastructure broken** – JUnit annotations not resolving (README.md notes attempted fixes)
3. **String-based FK** – Team-player relationship via `teamName` string, not proper foreign key
4. **No validation** – Entities accept any input; add `@Valid`, `@NotNull` if needed
5. **No error handling** – Controllers/services lack exception handlers; return null or generic error strings
6. **H2 transient** – All data lost on restart; no persistence
7. **Java 1.8** – Legacy version; avoid Java 9+ features

---

## AI Agent Hints

- **For endpoint changes:** Modify controller → service static method → repository query (in order)
- **For data changes:** Entity changes cascade naturally via H2 migrations on boot
- **For testing:** Write integration tests using Spring's TestRestTemplate when infrastructure is fixed
- **For logging:** Leverage SLF4J pattern in main application class
- **For new features:** Follow static service pattern unless explicitly refactoring architecture
- **For REST contracts:** Check existing controllers; maintain path parameter naming (e.g., `{teamId}`, `{playerId}`)

