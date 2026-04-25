# Junie Guidelines

## Priorities
- Speed
- Accuracy
- Available Documentation (.md files)

## Maven Testing Guidelines
- Run high level tests first.
- Ignore Jacoco coverage reports, unless otherwise specified.
- Shorten command outputs to focus on the most relevant information.

## Output File Analysis
- When reading output files (e.g., test results, logs), only consider the last 200 lines to focus on the most relevant information and final results.

## Other Guidelines
- Do not use "tail"
- Use surefire reports for errors instead of output