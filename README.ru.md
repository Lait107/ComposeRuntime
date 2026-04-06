# Compose Runtime as a Logic Engine (State Machine)
[🇷🇺 Русский](README.ru.md) | [🇺🇸 English](README.md)

Экспериментальный Android-проект, демонстрирующий использование **Jetpack Compose Runtime** вне графического интерфейса для управления бизнес-логикой приложения.

## 📖 Концепция

Традиционно Jetpack Compose воспринимается исключительно как инструмент для создания UI. Однако, согласно мысли, описанной в статье [A Jetpack Compose by Any Other Name](https://jakewharton.com/a-jetpack-compose-by-any-other-name/) Джейка Уортона, Compose состоит из двух независимых частей:

1.  **Compose UI:** Набор визуальных компонентов (Text, Column, Button).
2.  **Compose Runtime:** Мощный декларативный движок для управления деревом объектов, отслеживания состояний и управления побочными эффектами (Effects).

Этот проект использует **Runtime** как "движок предсказания состояний", превращая императивную логику в декларативную стейт-машину.

---

## 🎨 Сравнение подходов

### 1. Традиционный подход (ViewModel + Flow)
В классической архитектуре объединение нескольких источников данных и обработка событий часто превращается в сложную цепочку реактивных операторов:
```kotlin
// Традиционный способ (риск "лапши" из операторов)
val state = combine(flowA, flowB, eventFlow) { a, b, event -> 
    when(event) {
        is Login -> performLogin(a, b)
        else -> Initial
    }
}.stateIn(scope, SharingStarted.Lazily, Initial) 
```
### 2. Подход "Compose Runtime"
Логика описывается декларативно. Вместо операторов flatMapLatest, combine или zip используются обычные if, when и стандартные переменные:
```kotlin 
// Подход этого проекта
@Composable
fun AuthStateMachine(event: AuthEvent, repo: IAuthRepository) {
    var state by remember { mutableStateOf<AuthState>(...) }
    // Логика выглядит как обычный код, а не цепочка колбэков
    LaunchedEffect(event) {
        // Автоматическая отмена старой корутины при новом event
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
Смысл: Мы пишем логику так же просто, как UI. Вместо реактивных операторов (map, flatMap) — обычные if, when и LaunchedEffect + нам не нужна ViewModel

## 🛡 Structured Concurrency "из коробки" 
В традиционной ViewModel повторный вызов события требует ручного управления Job (отмена старого запроса). В данном подходе LaunchedEffect(event) автоматически отменяет предыдущую корутину при поступлении нового события, предотвращая "гонку состояний" (Race Conditions) без написания лишнего кода

## 🛠 Техническая реализация
Система состоит из трех ключевых уровней:

    1. AuthInteractor: Создает среду для работы Compose, он инициализирует Recomposer и управляет жизненным циклом Composition.
    2. AuthStateMachine: Composable-функция с логикой, отвечает за состояние UI.

## 🔄 Схема работы
    1. Вы вызываете interactor.send(Event).
    2. Composition пересчитывается (рекомпозиция).
    3. LaunchedEffect реагирует на новый Event и запускает запрос.
    4. Результат сохраняется в mutableStateOf.
    5. Измененное состояние через колбэк попадает в StateFlow, на который подписан UI.