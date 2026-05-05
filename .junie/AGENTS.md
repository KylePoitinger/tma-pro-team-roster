# TMA Pro Team Roster - Junie Guidelines

## Quick-Start Checklist
- [ ] Use **PowerShell** for all terminal commands (Windows environment).
- [ ] Add newly created files to git (`git add <file>`).
- [ ] Run `mvn clean install` for a full build and to check JaCoCo coverage.
- [ ] Maintain **80% line coverage** and **70% branch coverage** for Services and Controllers.
- [ ] Use the **Electron Launcher** (`src/main/electron-launcher`) to manage multiple services.

## Tech Stack
- **Backend**: Java 21, Spring Boot 3.3.0, Spring Data JPA, Maven.
- **Frontend**: Node.js (Manager Portal), Electron (Launcher).
- **Database**: SQLite (`pro_team_roster.db`) by default.
- **Integration**: Apache Kafka (disabled by default in properties).

## Common Commands

| Task | Command |
| :--- | :--- |
| **Build Project** | `mvn clean install` |
| **Run Backend** | `mvn spring-boot:run` |
| **Run Tests** | `mvn test` |
| **Manager Portal** | `cd src/main/nodejs/manager-portal; npm install; npm start` |
| **Electron Launcher**| `cd src/main/electron-launcher; npm install; npm start` |

## Architecture & Patterns
- **Three-Layer Pattern**: `Controller` → `Service` → `Repository`.
- **Entities**: JPA annotated, manual getters/setters/toString (No Lombok).
- **JSON Serialization**: Use `@JsonManagedReference` and `@JsonBackReference` to prevent recursion.
- **Testing**: JUnit 5, Mockito. Use `BaseIntegrationTest` for shared Spring context.

## Documentation
- Detailed instructions: `.github/copilot-instructions.md`
- Project history: `.github/CHANGELOG.md`
- Architecture Deep-Dive: `.github/skills/SKILLS.md`
