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
- **ProMascotEntity**: Mascots linked to a Team (`@OneToOne`).
- **ProPlayerEntity**: Players linked to a Team (`@ManyToOne`).
- **ProScheduleEntity**: Schedules linking a Team and an Arena (`@ManyToOne` for both).
- **ProTeamEntity**: Teams associated with an Arena (`@ManyToOne`). Has a list of Players (`@OneToMany`) and a Mascot (`@OneToOne`).
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
- **Swagger Documentation:**
    - **UI:** http://localhost:8080/swagger-ui.html
    - **API Docs (OpenAPI 3):** http://localhost:8080/v3/api-docs
- **Changelog:** [.github/CHANGELOG.md](.github/CHANGELOG.md) (Records all notable architectural and feature changes).
- **Initialization:** Automatic via `ProTeamRosterApplication.run()` (implements CommandLineRunner).
- **Scale:** Seeds 5 arenas, 8 teams, 11 players per team (88 total), 8 mascots, and 8 schedules on startup.
- **Timezone:** America/New_York (set in main method)
### Testing Strategy
- **JaCoCo Coverage:** Run JaCoCo coverage every time a new full build is made to ensure code quality and coverage.
    - **Command:** `mvn clean install` (automatically runs tests and generates coverage) or `mvn test`.
    - **Report Location:** `target/site/jacoco/index.html`
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
- `DELETE /arenas/{arenaId}`: API Endpoint
- `DELETE /mascots/{mascotId}`: API Endpoint
- `DELETE /schedules/{scheduleId}`: API Endpoint
- `DELETE /teams/{teamId}`: API Endpoint
- `DELETE players/{playerId}`: API Endpoint
- `GET /arenas`: API Endpoint
- `GET /arenas/{arenaId}`: API Endpoint
- `GET /health`: Returns service status and component health
- `GET /images`: API Endpoint
- `GET /mascots/team`: API Endpoint
- `GET /mascots/{mascotId}`: API Endpoint
- `GET /players/{playerId}`: API Endpoint
- `GET /schedules` – returns schedules with nested Team and Arena data.
- `GET /schedules/arena/{arenaId}`: API Endpoint
- `GET /schedules/team/{teamId}` – filtered schedules.
- `GET /schedules/{scheduleId}`: API Endpoint
- `GET /teams` – list all  
- `GET /teams/fields`: API Endpoint
- `GET /teams/{teamId}/roster` – team + players
- `POST /arenas`: API Endpoint
- `POST /mascots`: API Endpoint
- `POST /players`: API Endpoint
- `POST /schedules`: API Endpoint
- `POST /teams`: API Endpoint
- `PUT /arenas/{arenaId}`: API Endpoint
- `PUT /mascots/{mascotId}`: API Endpoint
- `PUT /players/{playerId}`: API Endpoint
- `PUT /schedules/{scheduleId}`: API Endpoint
- `PUT /teams/{teamId}`: API Endpoint

### Entity Annotations
- Use Lombok `@Data` (generates getters, setters, toString, etc.).
- `@Entity` for JPA mapping.
- All relationships are now proper JPA mappings (no more string-based "teamName" references).
---
## External Dependencies & Versions
| Dependency | Version | Scope | Purpose |
|-----------|---------|-------|---------|
| Spring Boot | 2.4.4 | compile | Core framework + autoconfiguration |
| Springdoc OpenAPI | 1.5.7 | compile | OpenAPI 3 / Swagger Documentation |
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
4. **Python Integration** – A separate Python dashboard is located in `src/main/python`. It uses Streamlit for data visualization.
---
## Python Analytics (Experimental)
### Prerequisites
- **Python 3.7+** must be installed (download from [python.org](https://www.python.org/downloads/)).
- Ensure Python and pip are in your system PATH.

### Setup & Run
1. **Install dependencies:**
   ```powershell
   cd src/main/python
   python -m pip install -r requirements.txt
   ```
2. **Run the dashboard:**
   ```powershell
   python -m streamlit run dashboard.py
   ```
   *Note: Using `python -m` ensures you use the version of streamlit linked to your current Python installation.*
### Purpose
Provides "visually cool" analytics and interactive dashboards for the Team Roster data, demonstrating how to integrate Python with a Java-based backend.
---
## AI Tool Usage & Logging
### Mandatory Changelog Updates
Every time an AI tool (Junie or GitHub Copilot) is used to generate or modify code, the developer MUST update `.github/CHANGELOG.md`.

### Agent Distinction
- **GitHub Copilot:** Use this agent name for code completions, chat-based suggestions, or small refactors initiated via the Copilot plugin. Entries should be placed under the `### GitHub Copilot` section.
- **Junie:** Use this agent name for autonomous task execution, multi-file refactors, or complex feature implementations performed by Junie. Entries should be placed under the `### Junie` section.

### Entry Format
Add a new row to the appropriate table using the following structure:
| timestamp | agent | action | files | summary | details |
| :--- | :--- | :--- | :--- | :--- | :--- |

- **timestamp:** ISO 8601 format (e.g., `2026-04-25T17:23:00`).
- **agent:** Either `GitHub Copilot` or `Junie`.
- **action:** `created`, `modified`, `deleted`, `renamed`, `expanded`, or `enhanced`.
- **files:** Backtick-enclosed file paths (e.g., `src/main/java/service/ProTeamService.java`). Use commas for multiple files.
- **summary:** Short description of the change.
- **details:** Comprehensive explanation of what was changed and why.

---
## AI Agent Hints
- **For changes:** Modify controller → service → repository (in order).
- **For data scale:** Check `ProTeamRosterApplication.java` for the current seeding logic.
- **For testing:** Leverage `TestRestTemplate` in integration tests.
- **For JSON:** Always check for circular references when adding new relationships.
