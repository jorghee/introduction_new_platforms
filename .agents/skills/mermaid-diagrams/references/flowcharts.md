# Flowcharts & Process Workflows

Flowcharts visualize algorithms, business logic, user journeys, and engineering processes. In Mermaid, flowcharts are declared using `flowchart TD` (top-to-bottom) or `flowchart LR` (left-to-right).

## Standard Node Shapes

Always quote text inside non-standard shapes to prevent syntax breaks on GitHub:

| Shape Syntax | Appearance | Semantic Use Case |
|---|---|---|
| `id["Process Text"]` | Rectangle | Standard task, computation, or operation |
| `id(["Start / End"])` | Stadium | Entry points, start states, termination points |
| `id{"Condition?"}` | Rhombus / Diamond | Decision points with branching pathways |
| `id[("Database Store")]` | Cylinder | Persistent storage or local cache access |
| `id[/"Input or Output"/]` | Parallelogram | User input or external output data |
| `id(("Connector"))` | Circle | Phase markers or join points |

---

## Example 1: Git Team Development Workflow

Documents the mandatory lifecycle from GitHub Issue assignment to main branch merge defined in `CONTRIBUTING.md`.

```mermaid
flowchart TD
    Start(["Issue Assigned (#N)"]) --> CreateBranch["Create Branch\n(type/N-short-description)"]
    CreateBranch --> Develop["Implement Solution in Kotlin\n(Follow compose-kotlin-agent-skills)"]
    Develop --> LocalBuild{"./gradlew build\npasses?"}

    LocalBuild -->|No| FixErrors["Fix Compilation / Lint Errors"]
    FixErrors --> Develop
    LocalBuild -->|Yes| CommitChanges["Commit Changes\n(type(scope): description in English)"]

    CommitChanges --> OpenPR["Open Pull Request on GitHub\n(Spanish title, template filled, link #N)"]
    OpenPR --> AutomatedCI{"CI Verification Gate\n(lint, build, tests)"}

    AutomatedCI -->|Failure| ReviewFixes["Address CI Failure on Branch"]
    ReviewFixes --> CommitChanges
    AutomatedCI -->|Success| PeerReview{"Code Review\n(android-reviewer / peers)"}

    PeerReview -->|Changes Requested| Refactor["Apply Requested Changes"]
    Refactor --> CommitChanges
    PeerReview -->|Approved| MergeSquash["Merge via Rebase & Fast-Forward\n(or Squash Merge)"]

    MergeSquash --> DeleteBranch["Delete Feature Branch"]
    DeleteBranch --> CloseIssue(["Issue #N Automatically Closed"])
```

---

## Example 2: Offline-First Data Synchronization Algorithm

Illustrates how AlertaUNI resolves local cache reads and background network synchronization.

```mermaid
flowchart TD
    Request(["Screen Requests Course Feed"]) --> ReadCache["Query Local Room Database"]
    ReadCache --> EmitLocal["Emit Cached Course List to UI StateFlow"]

    EmitLocal --> CheckConnectivity{"Network Connected?"}
    CheckConnectivity -->|No| KeepLocal(["Display Offline Banner with Cached Data"])

    CheckConnectivity -->|Yes| FetchRemote["Execute HTTP GET /courses on Supabase"]
    FetchRemote --> NetworkResult{"API Response Status"}

    NetworkResult -->|200 OK| CompareETag{"Data Changed\nvs Cache?"}
    NetworkResult -->|304 Not Modified| CompleteNoOp(["Keep Current Cache State"])
    NetworkResult -->|4xx / 5xx Error| HandleError["Log Error to Sentry\nEmit Error SnackBar Effect"]

    CompareETag -->|Yes| UpdateRoom["Execute Room Transaction:\nInsert new & update existing records"]
    CompareETag -->|No| CompleteNoOp

    UpdateRoom --> FlowNotify["Room Flow Triggers Invalidation"]
    FlowNotify --> EmitFreshState(["Emit Updated UiState to Screen"])
```

---

## Best Practices for Flowcharts

1. **Explicit Branch Labels**:
   - Always label branching arrows exiting a decision node: `-->|Yes|` and `-->|No|`.
2. **Consistent Orientation**:
   - Standardize on `TD` (Top-to-Bottom) for hierarchical workflows and decision trees.
   - Use `LR` (Left-to-Right) only when the process is strictly linear and has few branches.
3. **Avoid Dangling Paths**:
   - Every condition path must eventually terminate at a designated terminal node or join a common resolution node.
4. **Clean Technical Text**:
   - Never use emojis or visual icons in node text or branch conditions. Use technical terms matching the codebase.
