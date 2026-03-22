# Project specification

## **Code Style**
Follow the existing code style strictly:
- **Indentation**: 4 spaces, no tabs.
- **Naming**:
    - Classes, interfaces, objects: `PascalCase`.
    - Functions, variables, properties: `camelCase`.
    - Constants: `SCREAMING_SNAKE_CASE`.
    - JSON field names: always `snake_case`, enforced via `@SerialName("snake_case")` on serializable properties. Kotlin property names remain in `camelCase`.
    - Private backing properties (e.g., for delegated lazy initialization): prefixed with `_` (e.g., `_root`).
- **Braces**: K&R style — opening brace on the same line as the declaration (`if`, `fun`, `class`, etc.).
- **Spacing**:
    - One space after keywords (`if`, `when`, `try`, etc.).
    - No extra spaces inside parentheses or brackets unless required for readability.
    - Avoid trailing whitespace.
- **Imports**: Explicit, no wildcards. Grouped logically (standard lib → third-party → project).
- **Line breaks**: Prefer short lines (<120 chars). Break long argument lists or chained calls with one item per line if needed.
- **Serialization**: Use `kotlinx.serialization` with explicit `@Serializable` and `@SerialName`. Prefer typed data models over raw JSON.
- **String literals**: Avoid unnecessary spaces inside strings.
- **Comments**: Use sparingly; prefer self-explanatory code. Log meaningful messages for ops/debugging.

## Git Workflow & Conventional Commits
- Branches:
    - `master`: production-ready, always deployable.
    - `dev`: integration branch.
    - Feature branches: named like `a5`, `a123`, branched from and merged into `dev`.
- All changes **must** go through a Pull Request targeting `dev`.
- PR title **becomes the commit message** (due to squash merge) and **MUST** follow [Conventional Commits 1.0.0](https://www.conventionalcommits.org/en/v1.0.0/).

#### Commit Format:
```
<type>(<scope>): <description>
[optional body]
[optional footer]
```

- **Imperative mood**: “add”, “fix”, “update” — never “added” or “fixed”.
- **Scopes** must match actual Gradle modules: `theme`, `tracker`.
- **Types**: `feat`, `fix`, `docs`, `refactor`, `test`, `build`, `ci`, `chore`.
- **Breaking changes**: append `!` after type/scope **and** include a `BREAKING CHANGE:` footer. Required for any incompatible change in public API.

#### PR Requirements:
Before submission, contributors must:
1. Rebase onto latest `dev`.
2. Pass `./gradlew build`.
3. Pass all tests.
4. For bug fixes: add a test that reproduces the issue (unless impractical).
5. Comply with Code Style section and architectural boundaries.