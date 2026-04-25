# TMA Pro Team Roster

A Spring Boot REST API for managing professional sports teams and player rosters. Built with a three-layer architecture (Controller → Service → Repository) using an in-memory H2 database.

## Features

- **Team Management**: CRUD operations for professional teams
- **Player Management**: Manage players with team associations
- **Mascot Management**: Handle team mascots and images
- **Arena Management**: Venues for team games
- **RESTful API**: Full REST endpoints for all entities

## Architecture

### Three-Layer Pattern
1. **Controllers** (`src/main/java/controller/`): REST endpoints
2. **Services** (`src/main/java/service/`): Business logic with static methods
3. **Repositories** (`src/main/java/repository/`): JPA data access
4. **Entities** (`src/main/java/entity/`): JPA domain models

### Key Design Decisions
- Static service methods for simplicity
- String-based relationships (e.g., `teamName` in players)
- Public fields in entities with Lombok `@Data`
- In-memory H2 database with auto-migration

## Quick Start

### Prerequisites
- Java 1.8
- Maven 3.x

### Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080` with seeded data.

### Database
- **H2 Console**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: (empty)

## API Endpoints

### Teams
- `GET /teams` - List all teams
- `GET /teams/{teamId}` - Get team with roster
- `POST /teams` - Create team
- `PUT /teams/{teamId}` - Update team
- `DELETE /teams/{teamId}` - Delete team

### Players
- `GET /players/{playerId}` - Get player
- `POST /players` - Create player
- `PUT /players/{playerId}` - Update player
- `DELETE /players/{playerId}` - Delete player

### Mascots
- `GET /mascots/{mascotId}` - Get mascot
- `GET /mascots/team?team-name=X` - Get mascots by team
- `POST /mascots` - Create mascot
- `PUT /mascots/{mascotId}` - Update mascot
- `DELETE /mascots/{mascotId}` - Delete mascot

### Arenas
- `GET /arenas` - List all arenas
- `GET /arenas/{arenaId}` - Get arena
- `POST /arenas` - Create arena
- `PUT /arenas/{arenaId}` - Update arena
- `DELETE /arenas/{arenaId}` - Delete arena

## Development

### Project Structure
```
src/
├── main/java/
│   ├── controller/     # REST controllers
│   ├── service/        # Business logic (static methods)
│   ├── repository/     # JPA repositories
│   └── entity/         # JPA entities
└── test/java/          # Unit tests
```

### Testing
Tests are currently not fully wired due to Spring context issues. Use manual REST testing via HTTP clients instead.

### Adding New Services
Use the ServiceGenerator agent pattern:
1. Create Entity with `@Data` and public fields
2. Create Repository extending `JpaRepository`
3. Create Service with static methods
4. Create Controller with REST mappings
5. Add unit tests with Mockito

## Documentation

- [SKILLS.md](.github/SKILLS.md) - Detailed architecture analysis
- [AGENTS.md](.github/AGENTS.md) - Agent creation guide

## Technologies

- **Java**: 1.8
- **Spring Boot**: 2.4.4
- **Spring Data JPA**: For data access
- **H2 Database**: In-memory database
- **Lombok**: Boilerplate reduction
- **JUnit 4**: Testing framework
- **Mockito**: Mocking framework

## Known Issues

1. Test infrastructure not properly configured
2. Static service methods (intentional design choice)
3. String-based foreign keys instead of proper FKs
4. No input validation or error handling
5. Transient H2 data (lost on restart)

## Contributing

Follow the established patterns in SKILLS.md. Use agents for code generation to maintain consistency.</content>
<parameter name="filePath">C:\Users\kylep\IdeaProjects\tma-pro-team-roster\README.md
