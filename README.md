# Poké Explorer App 📱

"A fully functional Pokémon Explorer app built with Jetpack Compose. It features real-time data fetching, local caching, and type-based filtering, engineered with Clean Architecture and an Offline-First approach."
## 🎥 Features

* **Browse by Type:** Filter Pokémon by 10 different elemental types (Fire, Water, Grass, etc.).
* **Offline Support:** Fully functional without an internet connection once data is loaded (cached via Room).
* **Search:** Filter Pokémon by name locally within the selected category.
* **Performance Optimized:**
    * **Manual Pagination:** Loads list data once and handles pagination locally to minimize network bandwidth.
    * **Parallel Fetching:** Uses Kotlin Coroutines (`async/await`) to fetch Pokémon details concurrently for maximum speed.
    * **Instant Images:** Deterministic URL generation for zero-latency image loading start.
* **Clean UI:** Built 100% with **Jetpack Compose** following Material Design 3 guidelines.

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture (Data/Domain/Presentation layers)
* **Dependency Injection:** Hilt
* **Asynchronous:** Coroutines & Flow
* **Network:** Retrofit & OkHttp
* **Local Database:** Room (SQLite)
* **Image Loading:** Coil
* **Testing:** JUnit4, MockK, Coroutines Test

## 🏗️ Architecture & Key Decisions

The project follows strict **Separation of Concerns**:

### 1. Offline-First Strategy (Single Source of Truth)
The app uses the **Repository Pattern**. The UI observes the Local Database (Room). The Repository fetches data from the API, saves it to the DB, and the DB emits the new data to the UI.
* *Why:* Ensures the app works flawlessly in bad network conditions and reduces API calls on configuration changes.

### 2. Smart Data Fetching
* **Client-Side Pagination:** The PokéAPI `/type/{id}` endpoint returns *all* Pokémon for a type at once. Instead of re-fetching the list on "Load More", the app caches the full list in the ViewModel and exposes chunks (pages) to the UI.
    * *Benefit:* Extremely fast "Load More" experience and zero redundant network usage.
* **Parallel Detail Fetching:** When displaying a list, the app needs extra details (Stats/Images) which are not in the main list response. These are fetched in parallel batches using `async` coroutines.
    * *Benefit:* 5x-10x faster loading times compared to sequential fetching.

### 3. UI Optimization
* **Stable Lists:** Used `key` in LazyColumns and `remember` for static data to prevent unnecessary recompositions and UI flickering during state updates.
* **Job Cancellation:** Implemented explicit job cancellation in the ViewModel to handle rapid Type switching, preventing race conditions where data from a previous request might overwrite current data.

## 🧪 Testing

Unit tests are included for the `PokemonViewModel` to ensure:
* Initial data loading works correctly.
* Switching types correctly clears the old list and fetches new data.
* State management handles Loading/Success states accurately.

## 🚀 Setup

1.  Clone the repository.
2.  Open in Android Studio (Ladybug or newer recommended).
3.  Sync Gradle.
4.  Run on Emulator or Device.

---
*Developed by Andreas Gkotsopoulos*
