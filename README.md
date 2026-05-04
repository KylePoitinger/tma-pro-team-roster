# TMA Pro Team Roster

A modern Spring Boot application for managing professional sports teams, rosters, and game schedules. This project features a robust Java backend, a Node.js Manager Portal, and a Python Analytics Dashboard.

## 🚀 Quick Start

### Prerequisites
- **Java 21**
- **Maven 3.8+**
- **Node.js 16+** & **npm 7+**
- **Python 3.7+**

### Build and Run
1. **Full Build:**
   ```powershell
   mvn clean install
   ```
2. **Start All Services:**
   ```powershell
   mvn spring-boot:run
   ```
   *This will start the Spring Boot backend and automatically launch the auxiliary services (Manager Portal & Analytics Dashboard) via the `ServiceLauncherAgent`.*

### Access Points
- **Backend API:** [http://localhost:8080](http://localhost:8080)
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Manager Portal:** [http://localhost:3000](http://localhost:3000) (Credentials: `manager` / `password`)
- **Analytics Dashboard:** [http://localhost:8501](http://localhost:8501)
- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 🏗️ Architecture

The project follows a clean **Three-Layer Architecture**:
- **Controller Layer:** REST endpoints for Arenas, Teams, Players, Mascots, and Schedules.
- **Service Layer:** Core business logic and Kafka event production.
- **Repository Layer:** Spring Data JPA with H2 in-memory storage.

### Key Features
- **Event-Driven:** Entity changes are published to Kafka topics (configurable).
- **Multi-Service Startup:** Centralized startup via Spring Boot `CommandLineRunner`.
- **ADA Compliant:** UI components follow WCAG AA standards.
- **Automated Documentation:** Up-to-date documentation index in `.github/`.

---

## 📚 Documentation

Detailed documentation is maintained in the `.github/` directory:

- **[Documentation Index](/README.md)** - **Start here!**
- **[AI Developer Guide](.github/copilot-instructions.md)** - Coding patterns and conventions.
- **[Architecture Deep-Dive](.github/skills/SKILLS.md)** - Visual flows and detailed patterns.
- **[Changelog](.github/CHANGELOG.md)** - Chronological history of project evolution.

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3.3.0, Java 21, Spring Data JPA, Apache Kafka.
- **Frontend (Manager):** Node.js, Express, EJS, Jest.
- **Analytics:** Python, Streamlit, Pandas.
- **Database:** H2 In-Memory.

---

## 🧪 Testing

```powershell
mvn test
```
- **Java:** JUnit 5, Mockito, Awaitility, JaCoCo.
- **Node.js:** Jest & JSDOM.
- **Kafka:** Embedded Kafka for integration testing.

---

© 2026 TMA Pro Team Roster Project.
