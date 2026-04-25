# Changelog

All notable changes to this project will be documented in this file, following the guidelines in \.github/HistoryAgent.md\.

## 2026-04-25
### Junie

| timestamp | agent | action | files | summary | details |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 2026-04-25T14:38:00 | Junie | modified | \src/main/java/entity/ProScheduleEntity.java\, \src/main/java/service/ProScheduleService.java\, \src/main/java/repository/ProScheduleRepo.java\ | Return nested data for schedules | Updated the \/schedules\ endpoint to return full player and arena objects instead of IDs by using JPA \@ManyToOne\ relationships. |
| 2026-04-25T14:32:00 | Junie | modified | \src/main/java/ProTeamRosterApplication.java\ | Populated all entity fields on startup | Updated the application startup to populate all available fields in entities, including the new ProScheduleEntity, with realistic data. |
| 2026-04-25T14:25:00 | Junie | modified | \src/test/java/main/java/integration/ScaleDataIntegrationTest.java\ | Fixed data pollution in scale test | Added \@DirtiesContext\ to ensure a clean database state for the scale verification test. |
| 2026-04-25T14:22:00 | Junie | created | \src/test/java/main/java/integration/ScaleDataIntegrationTest.java\ | Expanded data scale to 10 teams, 5 arenas, and 11 players per team | Created a new integration test to verify the system can handle larger data volumes as requested. |
| 2026-04-25T14:18:00 | Junie | modified | \src/test/java/main/java/service/ProScheduleServiceTest.java\ | Expanded ProSchedule service tests | Added test cases covering multiple players and arenas to ensure the service correctly handles diverse scheduling scenarios. |
| 2026-04-25T14:15:00 | Junie | created | \src/main/java/*/ProSchedule*.java\ | Created ProSchedule service | Implemented Entity, Repository, Service, and Controller for Player Schedules, including ticket prices and arena mappings. |
| 2026-04-25T14:05:00 | Junie | modified | \src/test/java/main/java/integration/EndpointIntegrationTest.java\ | Fixed Connection Refused error in integration tests | Switched to \WebEnvironment.RANDOM_PORT\ to ensure the application starts correctly during tests. |
| 2026-04-25T14:00:00 | Junie | created | \src/test/java/main/java/integration/EndpointIntegrationTest.java\ | Added endpoint integration tests | Created a new integration test suite to verify REST endpoints against a running Spring Boot instance. |
| 2026-04-25T13:45:00 | Junie | modified | \src/test/java/main/java/service/*.java\ | Added SLF4J log statements to test classes | Inserted \LOG.info\ calls in \ProTeamServiceTest\, \ProPlayerServiceTest\, \ProMascotServiceTest\, \ProArenaServiceTest\, and \MascotImageServiceTest\ for better visibility. |
| 2026-04-25T13:40:00 | Junie | renamed | \.github/copilot-instructions.md\ | Reverted rename of Copilot instructions | Renamed \.github/.copilot-instructions.md\ back to \.github/copilot-instructions.md\ to protect standard naming conventions. |
| 2026-04-25T13:35:00 | Junie | renamed | \.github/copilot-instructions.md\ | Renamed Copilot instructions to hidden file | Renamed \.github/copilot-instructions.md\ to \.github/.copilot-instructions.md\ per user request (later reverted). |
| 2026-04-25T13:44:00 | Junie | created | \CHANGELOG.md\ | Initialized project changelog | Created the \CHANGELOG.md\ file at the project root with the structure required by \HistoryAgent.md\. |

### GitHub Copilot

| timestamp | agent | action | files | summary | details |
| :--- | :--- | :--- | :--- | :--- | :--- |
| | | | | | |

