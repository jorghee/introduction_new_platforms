# Software Architecture Diagrams

This guide explains how to document system architecture, component layers, and cloud infrastructure using GitHub-compatible Mermaid syntax.

## Why Not Native C4 Directives?

GitHub Markdown does not bundle the `C4Context` or `C4Container` plugins. Attempting to use them produces broken code blocks. Instead, we use `flowchart TD` or `flowchart LR` with structured `subgraph` enclosures to achieve identical architectural clarity with 100% native GitHub compatibility.

## Architectural Levels

### Level 1: System Context Diagram

Visualizes the software system in its environment, including human users, external university systems, and third-party services.

```mermaid
flowchart TD
    subgraph Users["Users"]
        Teacher["Teacher\n(Academic Staff)"]
        Student["Student\n(Enrolled User)"]
    end

    subgraph SystemBoundary["AlertaUNI Mobile Platform"]
        AlertaUNI["AlertaUNI Android App\n(Kotlin + Jetpack Compose)"]
    end

    subgraph ExternalSystems["External Systems"]
        UNSA_SIS["UNSA Academic Portal\n(Course & Enrollment Source)"]
        FCM["Firebase Cloud Messaging\n(Push Notification Service)"]
        Supabase["Supabase Cloud Platform\n(Auth, Edge Functions, PostgreSQL)"]
    end

    Teacher -->|Posts announcements and grades| AlertaUNI
    Student -->|Checks course notifications| AlertaUNI
    AlertaUNI -->|Syncs academic records| UNSA_SIS
    AlertaUNI -->|Registers device token| FCM
    AlertaUNI -->|HTTPS API & Realtime WebSockets| Supabase
    Supabase -->|Triggers push notifications| FCM
```

### Level 2: Container / Layered Architecture Diagram

Decomposes the system into high-level deployment containers and architectural layers.

```mermaid
flowchart TD
    subgraph MobileDevice["Android Client Application"]
        subgraph Presentation["Presentation Layer (Jetpack Compose)"]
            Screens["Composable Screens\n(Feed, Courses, Contacts)"]
            ViewModels["MVI ViewModels\n(StateFlow + Channel effects)"]
            Screens -->|Intent Events| ViewModels
            ViewModels -->|UiState updates| Screens
        end

        subgraph Domain["Domain Layer"]
            UseCases["Domain Managers & UseCases\n(Business Validation)"]
            ViewModels -->|Executes| UseCases
        end

        subgraph Data["Data Layer"]
            Repo["Repositories\n(CourseRepo, PostRepo, AuthRepo)"]
            RoomDB[("Room Local DB\n(SQLite offline cache)")]
            RemoteDataSource["Remote Data Source\n(Supabase Ktor / REST client)"]

            UseCases -->|Queries / Mutations| Repo
            Repo -->|Read / Write cache| RoomDB
            Repo -->|Sync remote| RemoteDataSource
        end
    end

    subgraph Backend["Backend Infrastructure (Supabase)"]
        EdgeFunctions["Edge Functions\n(TypeScript / Deno runtime)"]
        AuthService["GoTrue Auth Service\n(JWT & Session Tokens)"]
        PostgresDB[("PostgreSQL Database\n(Row Level Security enabled)")]

        RemoteDataSource -->|HTTPS REST| EdgeFunctions
        RemoteDataSource -->|Auth requests| AuthService
        EdgeFunctions -->|Internal SQL| PostgresDB
        AuthService -->|User identities| PostgresDB
    end
```

### Level 3: Component Diagram (Data Layer Internals)

Focuses on the internal composition of a specific subsystem, such as offline-first synchronization.

```mermaid
flowchart LR
    subgraph Repository["PostRepositoryImpl"]
        SyncMgr["Sync Coordinator"]
        CachePolicy["Cache-First Policy"]
    end

    subgraph LocalStorage["Local SQLite"]
        PostDao["PostDao\n(Room)"]
        EntityTable[("posts table")]
        PostDao --> EntityTable
    end

    subgraph Network["Network Client"]
        SupabaseClient["Supabase API Client"]
        DTO["PostDto Mappers"]
    end

    SyncMgr --> CachePolicy
    CachePolicy -->|Emit cached Flow| PostDao
    CachePolicy -->|Check stale timeout| SupabaseClient
    SupabaseClient -->|Fetch JSON| DTO
    DTO -->|Convert to Entity| PostDao
```

## Best Practices for Architecture Diagrams

1. **Direction**:
   - Use `flowchart TD` for layered architecture (Presentation on top, Data in middle, Database/Backend at bottom).
   - Use `flowchart LR` for data pipelines or horizontal service communication.
2. **Explicit Subgraphs**:
   - Always group related components using `subgraph ID["Label"]` syntax.
   - Use descriptive alphanumeric IDs for subgraphs and quoted labels for visual clarity.
3. **Database Representation**:
   - Use cylinder shape syntax `[("Database Name")]` for persistent stores (Room, PostgreSQL, Redis).
4. **Boundary Clarity**:
   - Clearly delineate process or network boundaries (e.g. On-Device vs Cloud Backend) through separate subgraphs.
