# Entity-Relationship Diagrams (ERD)

Entity-Relationship Diagrams model relational database schemas, table structures, keys, and foreign-key cardinalities. Mermaid provides native support via `erDiagram`.

## Cardinality Notation

Mermaid uses Crow's Foot notation for relationships:

| Notation | Left Side Meaning | Right Side Meaning | Typical Meaning |
|---|---|---|---|
| `||--||` | Exactly one | Exactly one | One-to-One mandatory |
| `||--o|` | Exactly one | Zero or one | One-to-One optional |
| `||--o{` | Exactly one | Zero or more | One-to-Many optional |
| `||--|{` | Exactly one | One or more | One-to-Many mandatory |
| `o|--o{` | Zero or one | Zero or more | Optional parent to many |

---

## Example: AlertaUNI Relational Schema

Models the core entities for academic communication, enrollments, announcements, and push token registrations across Supabase PostgreSQL and Room SQLite cache.

```mermaid
erDiagram
    USERS ||--o{ ENROLLMENTS : "registers in"
    COURSES ||--o{ ENROLLMENTS : "contains"
    USERS ||--o{ POSTS : "authors"
    COURSES ||--o{ POSTS : "hosts"
    POSTS ||--o{ COMMENTS : "receives"
    USERS ||--o{ COMMENTS : "writes"
    USERS ||--o{ DEVICE_TOKENS : "owns"

    USERS {
        uuid id PK
        string email UK
        string full_name
        string role "teacher | student | admin"
        string university_code UK "CUI / Student ID"
        timestamp created_at
        timestamp updated_at
    }

    COURSES {
        uuid id PK
        string code UK "e.g. IDNP-2026B"
        string name
        int semester
        string academic_term "e.g. 2026-B"
        timestamp created_at
    }

    ENROLLMENTS {
        uuid id PK
        uuid user_id FK
        uuid course_id FK
        string enrollment_status "active | dropped | completed"
        timestamp enrolled_at
    }

    POSTS {
        uuid id PK
        uuid course_id FK
        uuid author_id FK
        string title
        string content
        string category "announcement | material | alert"
        boolean is_pinned
        timestamp created_at
        timestamp updated_at
    }

    COMMENTS {
        uuid id PK
        uuid post_id FK
        uuid author_id FK
        string body
        timestamp created_at
    }

    DEVICE_TOKENS {
        uuid id PK
        uuid user_id FK
        string fcm_token UK
        string device_platform "android"
        timestamp updated_at
    }
```

---

## Best Practices for ER Diagrams

1. **Entity Names**:
   - Use uppercase singular or plural nouns matching database tables (`USERS`, `COURSES`).
2. **Key Annotations**:
   - Always flag `PK` (Primary Key), `FK` (Foreign Key), and `UK` (Unique Key) after attribute names.
3. **Attribute Types**:
   - Use standard database data types (`uuid`, `string`, `int`, `boolean`, `timestamp`).
4. **Relationship Labels**:
   - Enclose relationship descriptors in double quotes: `||--o{ ENROLLMENTS : "registers in"`.
5. **No Emojis**:
   - Never use emoji icons in table headers or relationship text.
