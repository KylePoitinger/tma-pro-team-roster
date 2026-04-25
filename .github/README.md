# Documentation Index – TMA Pro Team Roster

This folder contains consolidated documentation for the TMA Pro Team Roster project. Use this index to navigate project-related guides.

---

## 📚 Documentation Files

### 1. **copilot-instructions.md**
**Purpose:** AI coding guidelines for developers using GitHub Copilot

**Contents:**
- Project overview and architecture overview
- Three-layer pattern explanation
- Data models (ProTeamEntity, ProPlayerEntity, ProMascotEntity)
- REST endpoint patterns
- Key conventions and gotchas (static services, string-based FKs)
- External dependencies and versions
- Common tasks (adding endpoints, modifying entities, logging)
- Known issues and constraints

**Best For:** Rapid onboarding, understanding design decisions, quick reference

**Read This First** ✓

---

### 2. **SKILLS.md**
**Purpose:** Comprehensive codebase analysis with visual hierarchies and data flows

**Location:** `.github/skills/SKILLS.md`

**Contents (10 Skills):**
- **Skill 1:** Project hierarchy overview
- **Skill 2:** Three-layer architecture with data flows
- Skill 3: Detailed Controller → Service → Repository patterns
- Skill 4: Test suite architecture (current limitations)
- Skill 5: Request flows by entity type
- Skill 6: Entity relationships and string-based FKs
- Skill 7: Code organization and design patterns
- Skill 8: H2 database schema
- Skill 9: Maven build configuration
- Skill 10: Common development workflows

**Best For:** Understanding complete system architecture, visual learners, debugging workflows

**Key Features:**
- ASCII diagrams showing request/response flows
- Side-by-side code examples with explanations
- Entity relationship diagrams
- Testing strategy recommendations
- Build/run command reference

---

### 3. **AGENTS.md**
**Purpose:** Standardized machine-readable instructions for AI agents

**Location:** `.github/agents/AGENTS.md`

**Contents:**
- Technical context for AI coding assistants
- Coding standards and project constraints
- Tool and environment specifications

**Best For:** AI agents (Cursor, Windsurf, Cline, etc.) to understand project-specific rules

---

### 4. **HistoryAgent.md**
**Purpose:** Guidelines for automated changelog management

**Location:** `.github/agents/HistoryAgent.md`

**Contents:**
- Change detection skills
- Log entry composition rules
- Changelog management (agent-specific sections)
- Execution patterns

---

### 5. **CHANGELOG.md**
**Purpose:** Chronological log of all code changes

**Location:** `.github/CHANGELOG.md`

**Best For:** Tracking project evolution and agent actions (Junie & GitHub Copilot)

---

## 🎯 Quick Navigation

### **I want to...**

- **Get started contributing** → Read `copilot-instructions.md`
- **Understand the complete architecture** → Read `SKILLS.md`
- **Learn about AI Agents** → Read `AGENTS.md`
- **View recent changes** → Check `CHANGELOG.md`
- **Understand logging/history rules** → Read `HistoryAgent.md`
- **Add a REST endpoint** → See `SKILLS.md` (Skill 10.1)
- **Add an entity field** → See `SKILLS.md` (Skill 10.2)
- **Debug issues** → See `SKILLS.md` (Skill 10.3)
- **Understand database schema** → See `SKILLS.md` (Skill 8)
- **View design patterns** → See `SKILLS.md` (Skill 7)

---

## 📋 Project Overview at a Glance

```
TECH STACK:
- Spring Boot 2.4.4
- Java 1.8
- Maven (pom.xml)
- H2 In-Memory Database
- Lombok annotations
- Spring Data JPA

ARCHITECTURE:
- Controller → Service → Repository → Entity → Database
- Three-layer pattern (separation of concerns)
- Static service methods (unusual but intentional)

ENTITIES:
- ProTeamEntity (teams, OneToMany players & mascots)
- ProPlayerEntity (players, String-based FK on teamName)
- ProMascotEntity (mascots, String-based FK on teamName)

API ENDPOINTS:
- /teams (CRUD)
- /players (CRUD)
- /mascots (CRUD)
- H2 console at http://localhost:8080/h2-console

BUILD & RUN:
- mvn clean install
- mvn spring-boot:run
- Application on http://localhost:8080
- Tests: mvn test (some tests exist in src/test but build may fail due to environment)

TESTING STRATEGY:
- Manual REST testing via curl/Postman
- H2 console for data verification
- JUnit tests available in src/test/java (partially wired)
```

---

## 🔗 File Relationships

```
copilot-instructions.md
    ↓ (Strategic Overview)
    └──→ Reference for all design decisions
         and architectural patterns

SKILLS.md
    ↓ (Detailed Implementation)
    ├──→ Explains all 10 architecture skills
    ├──→ Shows code examples & flows
    └──→ Provides debugging workflows

AGENTS.md
    ↓ (Automation Guide)
    ├──→ Instructions for creating AI agents
    ├──→ Standardizes machine-readable rules
    └──→ References SKILLS.md for patterns

HistoryAgent.md & CHANGELOG.md
    ↓ (Traceability)
    ├──→ Tracks all agent-driven changes
    └──→ Separates work by Junie vs Copilot
```

---

## 📝 Document Metadata

| Document | Location | Last Updated | Scope |
|----------|----------|--------------|-------|
| copilot-instructions.md | `.github/` | April 25, 2026 | AI Guidelines |
| SKILLS.md | `.github/skills/` | April 25, 2026 | Architecture Analysis |
| AGENTS.md | `.github/agents/` | April 25, 2026 | Agent Creation |
| HistoryAgent.md | `.github/agents/` | April 25, 2026 | Changelog Rules |
| CHANGELOG.md | `.github/` | April 25, 2026 | Change History |

---

## 🚀 Next Steps

1. **New to the project?**
   - Start with `copilot-instructions.md` (5 min read)
   - Then review `SKILLS.md` (15 min read)

2. **Developing with Agents?**
   - Read `AGENTS.md` for creation guidelines
   - Follow `HistoryAgent.md` for logging changes

3. **Implementing a feature?**
   - Follow patterns in `SKILLS.md` Skill 3
   - Reference endpoints in `copilot-instructions.md`

3. **Debugging issues?**
   - Check `SKILLS.md` Skill 10.3
   - Use H2 console for data inspection
   - Review entity relationships in `SKILLS.md` Skill 6

4. **Planning improvements?**
   - Cross-reference with `copilot-instructions.md` constraints
   - Review architectural patterns in `SKILLS.md`

---

## 📞 Questions?

Refer to the relevant documentation section or reach out to the development team with specific questions about:
- Architecture: See `SKILLS.md`
- AI Agents: See `AGENTS.md`
- Guidelines: See `copilot-instructions.md`
- Change History: See `CHANGELOG.md`

