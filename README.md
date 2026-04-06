# Compose Runtime as a Logic Engine (State Machine)
[🇷🇺 Русский](README.ru.md) | [🇺🇸 English](README.md)

An experimental Android project demonstrating the use of Jetpack Compose Runtime outside the graphical user interface (GUI) to manage application business logic.

📖 Concept
Traditionally, Jetpack Compose is perceived solely as a tool for building UI. However, as described in the seminal article A Jetpack Compose by Any Other Name by Jake Wharton, Compose consists of two independent parts:

1. **Compose UI:** A set of visual components (Text, Column, Button, etc.).
2. **Compose Runtime:** A powerful declarative engine for managing node trees, tracking state changes, and handling side effects (Effects).

This project leverages the Runtime as a "state prediction engine," transforming imperative logic into a declarative state machine.



---

## 🎨 Approach Comparison

### 1. Traditional Approach (ViewModel + Flow)
In classic architecture, merging multiple data sources and handling events often turns into a complex chain of reactive operators:
```kotlin
// Traditional way (risk of "operator spaghetti")
val state = combine(flowA, flowB, eventFlow) { a, b, event -> 
    when(event) {
        is Login -> performLogin(a, b)
        else -> Initial
    }
}.stateIn(scope, SharingStarted.Lazily, Initial) 
```
### 2. Compose Runtime Approach
Logic is described declaratively. Instead of flatMapLatest, combine, or zip, you use standard Kotlin control flow (if, when) and state variab
```kotlin 
// The approach used in this project
@Composable
fun AuthStateMachine(event: AuthEvent, repo: IAuthRepository) {
    var state by remember { mutableStateOf<AuthState>(...) }
    // Logic looks like standard sequential code rather than a callback chain
    LaunchedEffect(event) {
        // Old coroutines are automatically cancelled when a new event arrives
        when (event) {
            is AuthEvent.Login -> {
                repo.login(...)
                state = AuthState.LoggedIn
            }
            AuthEvent.Logout -> {
                repo.logout(...)
                state = AuthState.LoggedOut
            }
        }
    }
}
```
Core idea: We write logic as simply as UI. Instead of reactive operators, we use standard if/when blocks and LaunchedEffect. In this specific implementation, a traditional ViewModel becomes optional.

## 🛡 Structured Concurrency "Out of the Box"
In a traditional ViewModel, re-triggering an event often requires manual Job management (canceling the previous request). In this approach, LaunchedEffect(event) automatically cancels the previous coroutine as soon as a new event is received, preventing Race Conditions without any extra boilerplate.

## 🛠 Technical Implementation
The system consists of two key layers:

    1. AuthInteractor: Acts as the host for the Compose environment. It initializes the Recomposer, provides the necessary FrameClock, and manages the Composition lifecycle.
    2. AuthStateMachine: A Composable function that encapsulates the business logic and is responsible for generating the UI state.

## 🔄 Workflow
    1. The UI calls interactor.send(Event).
    2. A Recomposition is triggered within the Interactor.
    3. LaunchedEffect reacts to the new Event and executes the required logic (e.g., a network request).
    4. The result is stored in a mutableStateOf snapshot.
    5. The updated state is emitted via callback to a StateFlow, which the UI observes for rendering