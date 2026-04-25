# JaCoCo Configuration Guide

## Overview
This project uses **JaCoCo (Java Code Coverage)** to measure and enforce code coverage thresholds. Code coverage helps ensure that the codebase is well-tested and reduces the risk of bugs in production.

---

## Configuration Details

### pom.xml Setup
The JaCoCo Maven plugin is configured in `pom.xml` with three execution goals:

#### 1. **prepare-agent**
- Instruments bytecode during test compilation
- Captures execution data during test runs
- No specific configuration needed

#### 2. **report**
- Phase: `test` (runs after all tests complete)
- Generates HTML coverage reports in `target/site/jacoco/`
- Generates CSV data for machine parsing

#### 3. **check** (Coverage Enforcement)
- Phase: `test` (runs after report generation)
- Verifies coverage meets minimum thresholds
- **Fails the build** if coverage is below threshold

---

## Coverage Thresholds

### Current Rules
```
Line Coverage (Statements):    80% minimum
Branch Coverage (Conditions): 70% minimum
```

### Applied To
**Services and Controllers** (excluding specified classes)

### Example Thresholds
- ✅ **PASS**: 85% line coverage
- ❌ **FAIL**: 78% line coverage (below 80%)
- ✅ **PASS**: 72% branch coverage  
- ❌ **FAIL**: 68% branch coverage (below 70%)

---

## Class Inclusions & Exclusions

### Included Classes (With Coverage Requirements)
```xml
<includes>
    <include>main.java.service.*</include>
    <include>main.java.controller.*</include>
</includes>
```

**All service and controller classes must meet thresholds**

### Excluded Classes (No Coverage Requirements)
```xml
<excludes>
    <exclude>main.java.ProTeamRosterApplication</exclude>
    <exclude>main.java.controller.MascotImageController</exclude>
    <exclude>main.java.service.MascotImageService</exclude>
    <exclude>main.java.controller.HealthCheckController</exclude>
</excludes>
```

### Why Certain Classes Are Excluded

| Class | Reason |
|-------|--------|
| `ProTeamRosterApplication` | Spring Boot bootstrap code - not part of business logic |
| `HealthCheckController` | Contains TODO placeholder methods |
| `MascotImageController` | Third-party integration - external dependency |
| `MascotImageService` | Third-party integration - external dependency |

### Fully Excluded Packages (Not in Any Check)
- `main.java.entity.*` - Lombok boilerplate (getters/setters)
- `main.java.exception.*` - Generic exception handlers
- `main.java.repository.*` - JPA framework-generated code

---

## Coverage Types

### 1. **Line Coverage (Statement Coverage)**
Measures the percentage of source code lines that are executed during tests.

**Example:**
```java
public void processTeam(Team team) {
    if (team != null) {                    // Line 1 (executed?)
        System.out.println(team.getName()); // Line 2 (executed?)
    }
}
```

**Calculation:**
- Line 1: Executed in test → 50% coverage (1 of 2 lines)
- If both lines executed → 100% coverage

---

### 2. **Branch Coverage (Condition Coverage)**
Measures the percentage of decision paths (if/else, switch cases) executed.

**Example:**
```java
public String getStatus(int score) {
    if (score >= 90) {              // Branch 1 (executed?)
        return "A";
    } else if (score >= 80) {       // Branch 2 (executed?)
        return "B";
    } else {                         // Branch 3 (executed?)
        return "C";
    }
}
```

**Calculation:**
- Testing with score = 95: Only Branch 1 executed → ~33% branch coverage
- Testing with score = 95, 85, 70: All branches executed → 100% branch coverage

---

## Report Generation

### Automatic Report
Generated automatically when tests run:
```bash
mvn clean test
```

### Report Locations
- **HTML Report**: `target/site/jacoco/index.html` (Open in browser)
- **CSV Report**: `target/site/jacoco/jacoco.csv` (Machine-readable)
- **XML Report**: `target/site/jacoco/jacoco.xml` (CI/CD integration)

### Report Contents
- **Summary**: Overall project coverage
- **Package View**: Coverage by package
- **Class View**: Coverage by individual class
- **Source View**: Line-by-line coverage indicators

---

## Adding Tests to Maintain Coverage

### Best Practices

#### 1. **Test Both Success and Failure Paths**
```java
@Test
void testSuccessCase() {
    // Arrange: Set up the scenario that succeeds
    when(repo.findById(1L)).thenReturn(Optional.of(entity));
    
    // Act: Execute the code
    Entity result = service.getEntity(1L);
    
    // Assert: Verify the outcome
    assertNotNull(result);
}

@Test
void testFailureCase() {
    // Arrange: Set up the scenario that fails
    when(repo.findById(999L)).thenReturn(Optional.empty());
    
    // Act & Assert: Verify exception is thrown
    assertThrows(ResourceNotFoundException.class, 
        () -> service.getEntity(999L));
}
```

#### 2. **Test All Methods**
Every public method must have at least one test:
```java
public class ServiceTest {
    @Test void testMethod1() { }
    @Test void testMethod2() { }
    @Test void testMethod3() { }
    // One test per public method minimum
}
```

#### 3. **Test Branches**
If/else statements need tests for each branch:
```java
// Code to test
public String classify(int value) {
    if (value > 0) return "positive";
    else return "negative";
}

// Tests needed for both branches
@Test void testPositive() { assertEquals("positive", classify(5)); }
@Test void testNegative() { assertEquals("negative", classify(-5)); }
```

---

## Running Coverage Analysis

### Command Examples

#### Run All Tests with Coverage
```bash
mvn clean test
```

#### Run Only Service Tests
```bash
mvn test -Dtest=*Service*
```

#### Run Only Integration Tests
```bash
mvn test -Dtest=*IntegrationTest
```

#### Run Specific Test Class
```bash
mvn test -Dtest=ProTeamServiceTest
```

#### Skip Coverage Enforcement (For Debugging)
```bash
mvn test -Djacoco.skip=true
```

---

## Interpreting Coverage Reports

### Color Coding
- 🟢 **Green**: Covered (executed in tests)
- 🔴 **Red**: Not covered (not executed in tests)
- 🟡 **Yellow**: Partially covered (some branches not executed)

### CSV Report Columns
```
GROUP: Project name
PACKAGE: Java package name
CLASS: Class name
INSTRUCTION_MISSED: Number of untested statements
INSTRUCTION_COVERED: Number of tested statements
LINE_MISSED: Lines without coverage
LINE_COVERED: Lines with coverage
BRANCH_MISSED: Decision paths not tested
BRANCH_COVERED: Decision paths tested
METHOD_MISSED: Methods not called in tests
METHOD_COVERED: Methods called in tests
```

---

## Troubleshooting

### Issue: Build Fails - "Coverage checks have not been met"
**Cause**: Code coverage below 80% threshold
**Solution**: 
1. Open `target/site/jacoco/index.html` 
2. Identify uncovered code (shown in red)
3. Write tests for uncovered methods
4. Re-run `mvn clean test`

### Issue: Specific Class Has Low Coverage
**Check Coverage Report**: 
```bash
# Navigate to target/site/jacoco/index.html
# Click on package → class → source code
# View red-highlighted lines (not covered)
```

### Issue: Need to Skip Coverage Check Temporarily
```bash
# Skip just the check (tests still run)
mvn test -Djacoco.check.skip=true

# Or in CI/CD:
export MAVEN_OPTS="-Djacoco.check.skip=true"
mvn test
```

---

## Integration with CI/CD

### GitHub Actions Example
```yaml
- name: Run Tests with Coverage
  run: mvn clean test

- name: Fail if Coverage Below Threshold
  run: mvn jacoco:check
  
- name: Upload Coverage Reports
  uses: codecov/codecov-action@v3
  with:
    files: ./target/site/jacoco/jacoco.xml
```

---

## Maintenance Checklist

- [ ] Run `mvn clean test` before committing
- [ ] Review coverage report for any new red lines
- [ ] Add tests for any new public methods
- [ ] Ensure branch coverage (all if/else paths tested)
- [ ] Keep coverage threshold above 80%
- [ ] Document any class exclusions with clear rationale

---

## Additional Resources

### JaCoCo Documentation
- [Official JaCoCo Site](https://www.jacoco.org/)
- [Maven Plugin Configuration](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

### Code Coverage Best Practices
- [Code Coverage Guide](https://martinfowler.com/bliki/CodeCoverage.html)
- Unit test one logical unit per test
- Test edge cases and error conditions
- Aim for high coverage (80%+) but focus on quality

---

## Questions?

Refer to:
1. **Project Guide**: `.github/copilot-instructions.md`
2. **Coverage Report**: `.github/COVERAGE_REPORT.md`
3. **This File**: `.github/JACOCO_GUIDE.md`
4. **Test Examples**: `src/test/java/main/java/`

---

Last Updated: April 25, 2026

