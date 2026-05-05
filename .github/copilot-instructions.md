# GitHub Copilot Instructions – AI Coding Guide for tma-pro-team-roster
## Project Overview
Spring Boot REST API managing professional sports teams and player rosters. SQLite database with a three-layer architecture (Controller → Service → Repository). Primary goal: readability and maintainability for legacy code scenarios.
**Build/Test:** Maven (pom.xml) | **Language:** Java 21 | **Framework:** Spring Boot 3.3.0
---
## Architecture & Data Flow
### Three-Layer Pattern
1. **Controllers** (`src/main/java/controller/`) – REST endpoints with request routing.
2. **Services** (`src/main/java/service/`) – Business logic and CRUD operations. All services are Spring `@Service` beans.
3. **Repositories** (`src/main/java/repository/`) – JPA interfaces extending `JpaRepository`.
### Data Models & Relationships
- **ProArenaEntity**: Top-level entity for venues.
- **ProMascotEntity**: Mascots linked to a Team (`@OneToOne`). Includes `species` and dynamic `imageUrl`.
- **ProPlayerEntity**: Players linked to a Team (`@ManyToOne`).
- **ProScheduleEntity**: Schedules linking a `homeTeam` and an `awayTeam` to an `Arena` (`@ManyToOne` for all).
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
- **SQLite** database (default).
- **SQLite:** Enabled by default in `application.properties` (file: `pro_team_roster.db`) for external viewing via SQLite GUI tools.
- **H2 Console:** http://localhost:8080/h2-console (can be enabled for in-memory testing).
- **Swagger Documentation:**
    - **UI:** http://localhost:8080/swagger-ui.html
    - **API Docs (OpenAPI 3):** http://localhost:8080/v3/api-docs
- **Changelog:** [.github/CHANGELOG.md](.github/CHANGELOG.md) (Records all notable architectural and feature changes).
- **Initialization:** Automatic via `ProTeamRosterApplication.run()` (implements CommandLineRunner).
- **Scale:** Seeds 5 arenas, 8 teams, 11 players per team (88 total), 8 mascots, and 8 schedules on startup.
- **Timezone:** America/New_York (set in main method)
### Testing Strategy
- **JaCoCo Coverage:** Run JaCoCo coverage every time a new full build is made to ensure code quality and coverage.
    - **Thresholds:** Minimum **80% line coverage** and **70% branch coverage** for services and controllers. Build fails if coverage drops below these values.
    - **Command:** `mvn clean install` (automatically runs tests and generates coverage) or `mvn test`.
    - **Report Location:** `target/site/jacoco/index.html`
- **Integration Tests:** Use `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`.
- **Frontend Logic Tests (Node.js):** 
    - Uses `jest` and `jest-environment-jsdom`.
    - Tests are located in `src/main/nodejs/manager-portal/test/`.
    - Command: `npm test` inside the `manager-portal` directory.
- **Key Tests:** 
    - `EndpointIntegrationTest`: Verifies health and core entity endpoints.
    - `ScaleDataIntegrationTest`: Verifies the requested scale of data (8 teams, 5 arenas, 11 players/team).
    - `trade-modal.test.js`: Verifies trade popup logic, team filtering, and form submission.
- **Verification:** Always run `mvn test -Dtest="main.java.integration.*Test"` after architectural changes.
---
## Package Structure & Naming
- `main.java.controller` – REST endpoint handlers  
- `main.java.service` – Business logic layer  
- `main.java.entity` – JPA-annotated domain objects
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
- `GET /health/port`: API Endpoint
- `GET /images/random-mascot`: Proxies a random mascot image from an external service
- `GET /mascots`: API Endpoint
- `GET /mascots/random`: Returns a random mascot entity with a fresh image URL
- `GET /mascots/team?team-id={id}`: Filtered mascots by team
- `GET /mascots/{mascotId}`: API Endpoint
- `GET /players/{playerId}`: API Endpoint
- `GET /schedules` – returns schedules with nested home/away Team and Arena data.
- `GET /schedules/arena/{arenaId}`: API Endpoint
- `GET /schedules/team/{teamId}` – filtered schedules for both home and away appearances.
- `GET /schedules/{scheduleId}`: API Endpoint
- `GET /teams` – list all  
- `GET /teams/fields?team-name={name}&team-city={city}&team-mascot={mascot}`: Search teams by multiple criteria
- `GET /teams/{teamId}/roster` – team + players
- `POST /arenas`: API Endpoint
- `POST /mascots`: API Endpoint
- `POST /players`: API Endpoint
- `POST /schedules`: API Endpoint
- `POST /teams`: API Endpoint
- `PUT /arenas/{arenaId}`: API Endpoint
- `PUT /mascots/{mascotId}`: API Endpoint
- `PUT /players/{playerId}`: API Endpoint
- `PUT /players/{playerId}/trade/{teamId}`: API Endpoint
- `PUT /schedules/{scheduleId}`: API Endpoint
- `PUT /teams/{teamId}`: API Endpoint

### Entity Annotations
- Use manual getters, setters, and `toString()` (Lombok removed for compatibility).
- `@Entity` for JPA mapping.
- All relationships are proper JPA mappings (@ManyToOne, @OneToMany, @OneToOne).

### Kafka Integration
- **Producer:** `ProKafkaProducer` sends events (`ProEvent`) on entity creation/update/deletion.
- **Consumer:** `ProKafkaConsumer` listens for player events.
- **Configuration:** Controlled by `spring.kafka.enabled` in `application.properties`.
- **Testing:** Kafka is disabled by default in tests (`application-test.properties`) to speed up execution and avoid connection noise. Use `@EmbeddedKafka` for dedicated Kafka integration tests.

### Accessibility & UI Standards
- **ADA Compliance:** All new UI elements must meet WCAG AA standards.
- **Color Contrast:** Maintain high contrast ratios (use CSS variables for theme consistency).
- **Keyboard Navigation:** Ensure all interactive elements have visible focus states and are reachable via Tab.
- **ARIA Roles:** Use semantic HTML and ARIA attributes for dynamic components like modals and alerts.
- **Alt Text:** Provide descriptive `alt` text for all images and icons.
---
## External Dependencies & Versions
| Dependency | Version | Scope | Purpose |
|-----------|---------|-------|---------|
| Spring Boot | 3.3.0 | compile | Core framework + autoconfiguration |
| Springdoc OpenAPI | 2.5.0 | compile | OpenAPI 3 / Swagger Documentation |
| Lombok | (Removed) | N/A | Migrated to manual getters/setters |
| JaCoCo | 0.8.13 | test | Code coverage reporting and enforcement |
| H2 Database | (inherited) | runtime | In-memory database |
| SQLite JDBC | 3.45.3.0 | compile | SQLite database driver |
| Hibernate Community Dialects | 6.4.4.Final | compile | Community dialects (including SQLite) |
| JUnit 5 | (inherited) | test | Testing framework |
| Node.js | 16+ | runtime | Manager Portal runtime |
| npm | 7+ | build | Node.js package manager |
| Jest | 29+ | test | Frontend unit testing |
| JSDOM | 22+ | test | Browser environment for Jest |
| Streamlit | 1.56.0 | runtime | Python data visualization |
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
### Update Instructions
1. Run `python scripts/update_docs.py` to auto-detect new entities and REST endpoints.
2. Manually verify changes in `.github/copilot-instructions.md`.
---
## Known Issues & Constraints
1. **H2 transient** – All data lost on restart; no persistence.
2. **Java 21+** – Modern version; uses Jakarta EE 10.
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
Provides "visually cool" analytics and interactive dashboards for the Team Roster data.
### Best Practices
- **Layout:** Use `st.columns` for grid layouts (e.g., 2x2 for metrics) to prevent text cutoff.
- **Dataframes:** Use `st.column_config` for all `st.dataframe` calls to provide proper formatting and sizing.
- **Images:** Use `st.image(url, width="stretch")` (Note: `alt` parameter is not supported in v1.56.0).
- **Styling:** Custom styles are in `src/main/python/style.css`.
---
## Node.js Manager Portal
### Prerequisites
- **Node.js 16+** and **npm** must be installed.
- Ensure they are in your system PATH.
- Ensure the Java backend is running (port 8080).

### Setup & Run
1. **Install dependencies:**
   ```powershell
   cd src/main/nodejs/manager-portal
   npm install
   ```
2. **Run the portal:**
   ```powershell
   npm start
   ```
3. **Run tests:**
   ```powershell
   npm test
   ```
4. **Access the portal:**
   - **URL:** http://localhost:3000
   - **Credentials:** `manager` / `password`

### Features & Architecture
- **Theme Toggle:** Supports persistent Dark Mode via `public/js/theme-toggle.js` and `localStorage`.
- **Trade Modal:** Interactive player trading using `public/js/trade-modal.js`.
- **Static Assets:** All JS/CSS are served from the `public/` directory.
- **Testing:** Unit tests for frontend logic using Jest in the `test/` directory.

### Purpose
Allows managers to login and perform administrative tasks, such as trading players between teams via the backend REST API.

---

## Electron Launcher
### Prerequisites
- **Node.js 16+** and **npm** must be installed.
- Ensure they are in your system PATH.

### Setup & Run
1. **Install dependencies:**
   ```powershell
   cd src/main/electron-launcher
   npm install
   ```
2. **Run the launcher:**
   ```powershell
   npm start
   ```

### Features
- **Centralized Control:** Start/Stop Java, Python, and Node.js services.
- **Health Monitoring:** Real-time Kafka and SQLite connectivity checks.
- **Log Streaming:** Integrated stdout/stderr logs from all sub-processes.

---

## Troubleshooting Environment Issues
### npm/Node.js not recognized
If you encounter the error: `npm : The term 'npm' is not recognized...`, it usually means Node.js is installed but not in your system `PATH`.
- **Default Windows Path:** `C:\Program Files\nodejs`
- **Temporary Fix (PowerShell):**
  ```powershell
  $env:Path += ";C:\Program Files\nodejs"
  ```
- **Permanent Fix:** Add `C:\Program Files\nodejs` to your system environment variables.

---

## AI Tool Usage & Logging
### Mandatory Changelog Updates
Every time an AI tool (Junie or GitHub Copilot) is used to generate or modify code, the developer MUST update `.github/CHANGELOG.md`.

### Entry Format
Add a new row to the **Project Change History** table in `.github/CHANGELOG.md` using the following structure:
| timestamp | agent | action | files | summary | details |

- **timestamp:** ISO 8601 format (e.g., `2026-04-25T17:23:00`). Entries MUST be kept in descending chronological order (newest at the top).
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
- **For Shell:** Prefer PowerShell commands over Bash.
- **For Git:** Automatically add newly created files to git.
