# Sequence Diagrams

Sequence diagrams illustrate interactions and message exchanges between participants over time. They are ideal for documenting API authentication lifecycles, realtime event broadcasting, and multi-agent coordination flows.

## Core Syntax Rules for GitHub

1. Use `autonumber` at the top of the diagram to make steps easy to reference in text discussions and PR reviews.
2. Define all participants explicitly with `participant ID as Label` at the beginning of the block to control column order.
3. Use solid arrows with filled heads `->>` for synchronous calls and open dashed arrows `-->>` for return messages.
4. Use `alt / else / end` blocks for branching logic (e.g. success vs error scenarios).
5. Never use reserved syntax words (`loop`, `alt`, `opt`, `end`, `par`) as participant identifiers.
6. Do not include semicolons `;` in message labels.

---

## Example 1: User Authentication & JWT Exchange

Documents how the Android client signs in against Supabase and retrieves protected course data.

```mermaid
sequenceDiagram
    autonumber
    participant App as Android Client
    participant Auth as Supabase Auth (GoTrue)
    participant Edge as Edge Function (Courses)
    participant DB as PostgreSQL Database

    App->>Auth: signInWithPassword(email, password)
    activate Auth
    Auth->>DB: Query user credentials
    DB-->>Auth: Password hash & user metadata
    Auth->>Auth: Verify hash & sign JWT
    Auth-->>App: Return Session(accessToken, refreshToken)
    deactivate Auth

    App->>Edge: GET /courses (Bearer accessToken)
    activate Edge
    Edge->>Edge: Validate JWT signature & claims
    alt Valid Token
        Edge->>DB: SELECT * FROM courses WHERE student_id = uid
        DB-->>Edge: Course records
        Edge-->>App: 200 OK (CourseListResponse)
    else Expired or Invalid Token
        Edge-->>App: 401 Unauthorized (InvalidTokenError)
    end
    deactivate Edge
```

---

## Example 2: Realtime Post Notification Flow

Shows how a new post created by a teacher propagates via Supabase Realtime to enrolled students.

```mermaid
sequenceDiagram
    autonumber
    participant Teacher as Teacher App
    participant Edge as Supabase Edge Function
    participant DB as PostgreSQL Database
    participant Realtime as Supabase Realtime Engine
    participant Student as Student App

    Teacher->>Edge: POST /courses/{id}/posts
    activate Edge
    Edge->>DB: INSERT INTO posts (...) RETURNING id
    DB-->>Edge: Created record
    Edge-->>Teacher: 201 Created (PostDto)
    deactivate Edge

    DB->>Realtime: PostgreSQL WAL trigger (CDC)
    activate Realtime
    Realtime->>Student: WebSocket broadcast (channel: course_posts)
    deactivate Realtime

    activate Student
    Student->>Student: Parse incoming PostDto
    Student->>Student: Insert into local Room database
    Student->>Student: Update UI StateFlow
    deactivate Student
```

---

## Example 3: Antigravity Multi-Agent Orchestration Flow

Illustrates how the project's AI subagents collaborate from requirement parsing to verified implementation.

```mermaid
sequenceDiagram
    autonumber
    actor Human as Developer / Evaluator
    participant Orch as Orchestrator Agent
    participant Implementer as Android Implementer
    participant Reviewer as Android Reviewer

    Human->>Orch: Assign GitHub Issue (#10 Enrollment)
    activate Orch
    Orch->>Orch: Analyze Issue scope & acceptance criteria
    Orch->>Orch: Create decomposition & implementation plan

    Orch->>Implementer: Delegate Kotlin/Compose task
    activate Implementer
    Implementer->>Implementer: Consult compose-kotlin-agent-skills
    Implementer->>Implementer: Write Composables & ViewModel MVI
    Implementer->>Implementer: Verify compile & banned antipatterns
    Implementer-->>Orch: Implementation complete (code diff)
    deactivate Implementer

    Orch->>Reviewer: Request code review for changes
    activate Reviewer
    Reviewer->>Reviewer: Execute 6-point Compose checklist
    Reviewer->>Reviewer: Check 00-banned-antipatterns.md
    alt All Checks Pass
        Reviewer-->>Orch: Verdict: APPROVED
    else Antipattern or Lint Failure
        Reviewer-->>Orch: Verdict: CHANGES_REQUESTED (findings)
        Orch->>Implementer: Request fixes (iteration 1 of 3)
    end
    deactivate Reviewer

    Orch-->>Human: Present final solution & PR summary for approval
    deactivate Orch
```

---

## Best Practices for Sequence Diagrams

- **Focus**: Keep sequence diagrams strictly to one interaction flow or scenario. Do not combine disparate use cases into one diagram.
- **Activations**: Use `activate` and `deactivate` to explicitly show processing lifespans.
- **Notes**: Add explanatory context when necessary using `Note over Participant: Explanation`.
- **Clean Naming**: Ensure participant labels reflect accurate system roles (`Teacher App`, `PostgresDB`, `Orchestrator Agent`).
