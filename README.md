# TMA Pro Team Roster

A Spring Boot REST API for managing professional sports teams and player rosters with in-memory H2 database persistence.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Architecture](#architecture)
- [Known Issues](#known-issues)
- [Development](#development)

## Overview

TMA Pro Team Roster is a lightweight REST API built with Spring Boot for managing professional sports team rosters. The application demonstrates a clean three-layer architecture (Controller → Service → Repository) designed for readability and maintainability—essential qualities when working with legacy code.

## Features

- ✅ **Team Management** – Create, read, update, and delete professional sports teams
- ✅ **Player Roster** – Manage player information including salary, college, and physical attributes
- ✅ **Mascot Management** – Track team mascots with descriptions and costume details
- ✅ **In-Memory Database** – H2 database with automatic schema initialization
- ✅ **REST API** – Full CRUD operations with standard HTTP methods
- ✅ **Query Support** – Filter teams by name, city, or mascot

## Tech Stack

| Component | Version |
|-----------|---------|
| **Language** | Java 1.8 |
| **Framework** | Spring Boot 2.4.4 |
| **Build Tool** | Maven |
| **Database** | H2 (In-Memory) |
| **ORM** | Spring Data JPA |
| **Annotation Processing** | Lombok 1.18.30 |
| **Testing** | JUnit 5 |

## Getting Started

### Prerequisites
- Java 1.8 or higher
- Maven 3.6+

### Installation & Running

```bash
# Clone the repository
git clone <repository-url>
cd tma-pro-team-roster

# Full build
mvn clean install

# Start the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080` with sample data pre-loaded.

### H2 Database Console

Access the H2 console at: **http://localhost:8080/h2-console**
- Default user: `sa` (no password)

## Project Structure

```
src/main/java/
├── ProTeamRosterApplication.java
├── controller/
│   ├── ProTeamController.java
│   ├── ProPlayerController.java
│   └── ProMascotController.java
├── service/
│   ├── ProTeamService.java
│   ├── ProPlayerService.java
│   └── ProMascotService.java
├── repository/
│   ├── ProTeamRepo.java
│   ├── ProPlayerRepo.java
│   └── ProMascotRepo.java
└── entity/
    ├── ProTeamEntity.java
    ├── ProPlayerEntity.java
    └── ProMascotEntity.java

src/test/java/
├── ProTeamServiceTest.java
└── ProPlayerServiceTest.java
```

## API Endpoints

### Teams
- `GET /teams` – List all teams
- `GET /teams/{teamId}` – Get team details
- `GET /teams/{teamId}/roster` – Get team with players
- `GET /teams/fields?team-name=X&team-city=Y&team-mascot=Z` – Filter teams
- `POST /teams` – Create team
- `PUT /teams/{teamId}` – Update team (upsert)
- `DELETE /teams/{teamId}` – Delete team

### Players
- `GET /players/{playerId}` – Get player details
- `POST /players` – Create player
- `PUT /players/{playerId}` – Update player (upsert)
- `DELETE /players/{playerId}` – Delete player

### Mascots
- `GET /mascots/{mascotId}` – Get mascot details
- `GET /mascots/team?team-name=X` – Get mascots by team
- `POST /mascots` – Create mascot
- `PUT /mascots/{mascotId}` – Update mascot (upsert)
- `DELETE /mascots/{mascotId}` – Delete mascot

## Architecture

### Three-Layer Design
The application follows a clean three-layer architecture for separation of concerns:

1. **Controllers** – REST endpoints and HTTP request handling
2. **Services** – Business logic and CRUD operations (all static methods)
3. **Repositories** – Data access layer with Spring Data JPA

### Key Design Decisions

- **Static Service Methods** – All service methods are static for simplicity in this legacy-focused codebase
- **String-Based Relationships** – Team-player and team-mascot links use `teamName` string references (not foreign keys)
- **LAZY Loading** – One-to-many relationships use LAZY fetch to optimize queries
- **H2 In-Memory** – Automatic schema initialization on startup; data resets on application restart

For more details, see `.github/copilot-instructions.md`.

## Known Issues

### Test Infrastructure
- **JUnit Wiring Not Configured** – Test classes lack Spring `@SpringBootTest` and `@Autowired` context
- **Workaround** – Use manual REST testing via `mvn spring-boot:run` and curl/Postman
- **Future** – Fix test infrastructure using MockMvc, `@SpringBootTest`, or `@DataJpaTest`

### Other Constraints
- No input validation (add `@Valid`, `@NotNull` if needed)
- No comprehensive error handling (controllers return null or generic strings)
- No persistence (H2 in-memory; data lost on restart)
- Java 1.8 (legacy version; avoid Java 9+ features)

## Development

### Running Tests

```bash
# Run unit tests (currently not properly wired)
mvn test

# Manual testing via HTTP client
mvn spring-boot:run
# Then use curl or Postman to test endpoints
```

### Building for Production (Not Recommended)

This is a learning/legacy demonstration project. For production use:
- Configure a persistent database (PostgreSQL, MySQL, etc.)
- Add input validation and error handling
- Implement proper test infrastructure with mocks
- Upgrade to a modern Java version and Spring Boot LTS release

### Adding New Features

Refer to `.github/copilot-instructions.md` for AI agent hints on:
- Adding REST endpoints
- Modifying entities
- Working with the static service pattern
- Understanding the string-based relationship pattern

## License

[Add license information here, if applicable]
