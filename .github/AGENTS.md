# AGENTS.md – Agent Creation Guide for TMA Pro Team Roster

## Overview

This document outlines the process for creating specialized AI agents to assist with development tasks in the TMA Pro Team Roster project. Agents are designed to automate complex workflows, enforce project patterns, and improve productivity by delegating tasks to focused AI specialists.

## Agent Architecture

### Core Concepts
- **Agent**: A specialized AI assistant with defined skills and tools
- **Skill**: A specific capability (e.g., code generation, testing, documentation)
- **Task**: A user-defined objective that an agent can execute
- **Delegation**: Using `run_subagent` to invoke agents for specific tasks

### Available Agents
- **Plan**: Researches and outlines multi-step plans for complex tasks

## Creating New Agents

### Step 1: Define Agent Purpose
Identify the problem the agent will solve. Examples:
- ServiceGenerator: Creates new services based on descriptions
- TestGenerator: Generates comprehensive test suites
- RefactorAgent: Applies code refactoring patterns
- DocAgent: Updates documentation automatically

### Step 2: Specify Skills
Define the agent's capabilities based on project patterns from SKILLS.md:
- Read project structure (.md files)
- Analyze existing code
- Generate code following conventions
- Validate against architecture rules
- Update related files (tests, docs)

### Step 3: Implement Agent Logic
Agents follow this execution pattern:
1. **Input Processing**: Parse user description
2. **Context Gathering**: Read relevant .md files and code
3. **Pattern Analysis**: Understand project conventions
4. **Code Generation**: Create files following patterns
5. **Validation**: Ensure generated code compiles
6. **Integration**: Update related components

### Step 4: Agent Registration
Add the agent to the available agents list in the system configuration.

## Example Agent: ServiceGenerator

### Purpose
Creates complete service implementations (Entity + Repository + Service + Controller + Tests) based on user descriptions.

### Skills
1. **Structure Analysis**: Reads SKILLS.md to understand three-layer architecture
2. **Entity Design**: Creates JPA entities with appropriate fields and relationships
3. **Repository Creation**: Generates JPA repository interfaces with custom queries
4. **Service Implementation**: Builds static method services following project patterns
5. **Controller Development**: Creates REST endpoints with proper mappings
6. **Test Generation**: Produces unit tests with Mockito mocking
7. **Integration**: Ensures all components work together

### Usage Example
```
User: "Create a new service for managing game schedules"
Agent: 
- Analyzes SKILLS.md for patterns
- Creates ProScheduleEntity, ProScheduleRepo, ProScheduleService, ProScheduleController, ProScheduleServiceTest
- Validates compilation
- Returns success confirmation
```

## Agent Development Guidelines

### Code Generation Rules
- Follow Java 1.8 compatibility
- Use Lombok @Data for entities
- Implement static service methods
- Include proper error handling
- Add comprehensive tests

### File Structure
```
.github/
  AGENTS.md          # This file
  SKILLS.md          # Project patterns
  [agent-specific].md # Individual agent docs
```

### Testing Agents
- Validate generated code compiles
- Ensure tests pass
- Check integration with existing code
- Verify REST endpoints work

## Future Agent Ideas

1. **MigrationAgent**: Handles database schema migrations
2. **SecurityAgent**: Adds authentication/authorization
3. **PerformanceAgent**: Optimizes queries and caching
4. **APIAgent**: Generates OpenAPI documentation
5. **DeployAgent**: Manages deployment configurations

## Best Practices

- Always read SKILLS.md for current patterns
- Maintain backward compatibility
- Include error handling in generated code
- Generate tests for all new functionality
- Update documentation when creating agents
- Test agents thoroughly before deployment

## Agent Invocation

Use the `run_subagent` tool with appropriate parameters:
```
run_subagent(agentName="ServiceGenerator", task="Create arena service for team venues")
```

This ensures consistent agent behavior and proper task delegation.</content>
<parameter name="filePath">C:\Users\kylep\IdeaProjects\tma-pro-team-roster\.github\AGENTS.md