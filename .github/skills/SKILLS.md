# SKILLS.md - Project Architecture Analysis & Hierarchy
## Codebase Analysis: TMA Pro Team Roster

---

## Skill 1: Analyze Codebase & Project Hierarchy [Done]

### 1.1 Project Hierarchy Overview

```
tma-pro-team-roster (Maven Project)
|
+-- pom.xml                                    # Maven configuration (Java 21, Spring Boot 3.3.0)
|
+-- src/main/java                              # Source Code
|   +-- ProTeamRosterApplication.java          # Entry point + seeds DB
|   |
|   +-- config/                                # Configuration beans
|   |   +-- KafkaConfig.java                   # Kafka topic & property config
|   |
|   +-- controller/                            # Layer 1: REST Endpoints
|   |   +-- ProTeamController.java             # Team endpoints
|   |   +-- ProPlayerController.java           # Player endpoints
|   |   +-- ProMascotController.java           # Mascot endpoints
|   |   +-- ProArenaController.java            # Arena endpoints
|   |   +-- ProScheduleController.java         # Schedule endpoints
|   |   +-- HealthCheckController.java          # Health check endpoint
|   |   +-- MascotImageController.java          # Mascot image handling
|   |
|   +-- service/                               # Layer 2: Business Logic
|   |   +-- ProTeamService.java                # Team service
|   |   +-- ProPlayerService.java              # Player service
|   |   +-- ProMascotService.java              # Mascot service
|   |   +-- ProArenaService.java               # Arena service
|   |   +-- ProScheduleService.java            # Schedule service
|   |   +-- ProKafkaProducer.java              # Kafka producer
|   |   +-- ProKafkaConsumer.java              # Kafka consumer
|   |   +-- MascotImageService.java            # Image service
|   |
|   +-- repository/                            # Layer 3: Data Access (JPA)
|   |   +-- ProTeamRepo.java                   # Team repository
|   |   +-- ProPlayerRepo.java                 # Player repository
|   |   +-- ProMascotRepo.java                 # Mascot repository
|   |   +-- ProArenaRepo.java                  # Arena repository
|   |   +-- ProScheduleRepo.java               # Schedule repository
|   |
|   +-- entity/                                # Data Models (JPA Entities)
|       +-- ProTeamEntity.java                 # Team entity
|       +-- ProPlayerEntity.java               # Player entity
|       +-- ProMascotEntity.java               # Mascot entity
|       +-- ProArenaEntity.java                # Arena entity
|       +-- ProScheduleEntity.java              # Schedule entity
|
+-- src/test/java                              # Functional Tests
|   +-- main/java/integration/
|       +-- EndpointIntegrationTest.java       # Integration tests
|       +-- ScaleDataIntegrationTest.java      # Scale verification
|
+-- src/main/resources
    +-- application.properties                 # Spring Boot config (H2)
```

---

## Skill 2: Three-Layer Architecture with Data Flow

### 2.1 Request-Response Flow (HTTP   Controller   Service   Repository   Entity)

```
+--------------------------------------------------------------------------+
|                         HTTP CLIENT (curl, Postman, etc.)                |
+------------------------------------+-------------------------------------+
                                     |
                    HTTP Request (GET/POST/PUT/DELETE)
                    Path: /teams, /players, /mascots
                                     |
                                     V
         +-------------------------------------------------------------+
         |  LAYER 1: REST CONTROLLER (HttpRequest Handler)             |
         |  -----------------------------------------------------------|
         |  Example: ProTeamController.java                            |
         |                                                             |
         |  @RestController                                            |
         |  @GetMapping("/teams/{teamId}")                             |
         |  public ProTeamEntity getTeam(long teamId) {                |
         |    return proTeamService.getSingleTeam(teamId);             |
         |  }                                                          |
         |                                                             |
         |  Methods:                                                   |
         |  * Receive HTTP requests via @GetMapping, etc.             |
         |  * Extract path/query parameters                           |
         |  * Call Service layer                                      |
         |  * Return Entity or List response                          |
         +------------------------+------------------------------------+
                                  |
                    Service Layer Invocation
                    proTeamService.getSingleTeamAndRoster(teamId)
                                  |
                                  V
         +-------------------------------------------------------------+
         |  LAYER 2: SERVICE LAYER (Business Logic)                    |
         |  -----------------------------------------------------------|
         |  Example: ProTeamService.java                               |
         |                                                             |
         |  @Service                                                   |
         |  public class ProTeamService {                              |
         |    @Autowired                                               |
         |    private ProTeamRepo proTeamRepo;                         |
         |                                                             |
         |    public ProTeamEntity getSingleTeam(long teamId) {        |
         |      return proTeamRepo.getOneByTeamId(teamId);             |
         |    }                                                        |
         |  }                                                          |
         |                                                             |
         |  Design Pattern: Spring Beans                               |
         |  * standard @Service components                            |
         |  * Repositories autowired                                  |
         |  * Handles data transformation/validation                  |
         |  * Implements custom business logic                        |
         +------------------------+------------------------------------+
                                  |
                Repository Method Invocation
                proTeamRepo.getOneByTeamId(teamId)
                                  |
                                  V
         +-------------------------------------------------------------+
         |  LAYER 3: REPOSITORY (Data Access Layer - JPA)               |
         |  -----------------------------------------------------------|
         |  Example: ProTeamRepo.java                                  |
         |                                                             |
         |  @Repository                                                |
         |  public interface ProTeamRepo extends                       |
         |    JpaRepository<ProTeamEntity, Long> {                     |
         |                                                             |
         |    ProTeamEntity getOneByTeamId(long teamId);               |
         |    // Spring auto-derives query from method name            |
         |                                                             |
         |    @Query("delete from ProTeamEntity b...")                 |
         |    @Modifying                                               |
         |    @Transactional                                           |
         |    void deleteTeamById(@Param("teamId") long teamId);       |
         |  }                                                          |
         |                                                             |
         |  Features:                                                  |
         |  * Spring Data JPA interface (auto-implemented)             |
         |  * Auto-derived queries from method names                   |
         |  * Custom @Query annotations for complex ops               |
         |  * Transactional support for writes                        |
         +------------------------+------------------------------------+
                                  |
                    Database Query Execution
                    SQLite Database (pro_team_roster.db)
                                 |
                                 V
         +-------------------------------------------------------------+
         |  DATA MODELS: ENTITIES (JPA Persistence)                     |
         |  -----------------------------------------------------------|
         |  Example: ProTeamEntity.java                                |
         |                                                             |
         |  // manual getters/setters (no Lombok)                      |
         |  @Entity                        // JPA mapping              |
         |  public class ProTeamEntity {                               |
         |    @Id                                                      |
         |    public long teamId;          // Primary key              |
         |                                                             |
         |    public String name;          // Team name                |
         |    public String city;          // City location            |
         |    public String mascot;        // Mascot name              |
         |                                                             |
         |    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)    |
         |    @JsonManagedReference                                    |
         |    public List<ProPlayerEntity> proPlayers;                |
         |  }                                                          |
         |                                                             |
         |  Features:                                                  |
         |  * Manual methods: getters, setters, toString()            |
         |  * @Entity: JPA persistence mapping                        |
         |  * OneToMany: team has multiple players                    |
         |  * LAZY loading: optimal query performance                 |
         |  * Proper JPA relationships                                |
         +------------------------+------------------------------------+
                                  |
                     SQLite Database Table
                     CREATE TABLE pro_team_entity (
                       team_id BIGINT PRIMARY KEY,
                       name VARCHAR(255),
                       city VARCHAR(255),
                       mascot VARCHAR(255)
                     );
                                  |
                                  V
         +-------------------------------------------------------------+
         |  DATABASE: SQLITE                                           |
         |  -----------------------------------------------------------|
         |  * Persistence via pro_team_roster.db                       |
         |  * External viewing via SQLite GUI tools enabled           |
         |  * Seed data loaded via CommandLineRunner                   |
         |  * Schema managed by Hibernate (ddl-auto=update)            |
         |  * Kafka: Disabled by default (spring.kafka.enabled)        |
         +------------------------+------------------------------------+
                                  |
                    Response Returned to Repository
                          ProTeamEntity
                                  |
                                  V
         +-------------------------------------------------------------+
         |  Response Returned to Service Layer                         |
         |  Business logic applied if needed                           |
         |  Response Returned to Controller                            |
         |  HTTP Status + JSON Serialization (Entity)                  |
         +------------------------+------------------------------------+
                                  |
                                  V
+--------------------------------------------------------------------------+
|                   HTTP RESPONSE (JSON)                                   |
|                   Status: 200 OK                                         |
|                                                                          |
|  {                                                                       |
|    "teamId": 1,                                                          |
|    "name": "Hornets",                                                    |
|    "city": "Charlotte",                                                  |
|    "mascot": "Bee"                                                       |
|  }                                                                       |
+--------------------------------------------------------------------------+
```

---

## Quick Reference

- **Skill 3:** Detailed Controller   Service   Repository Flow
- **Skill 4:** Test Suite Architecture
- **Skill 5:** Request Flow by Entity Type
- **Skill 6:** Entity Relationships & String-Based Foreign Keys
- **Skill 7:** Code Organization & Patterns
- **Skill 8:** Database Schema (SQLite)
- **Skill 9:** Build & Deployment Configuration
- **Skill 10:** Common Development Workflows

For project-specific AI instructions, see: `.github/copilot-instructions.md`

