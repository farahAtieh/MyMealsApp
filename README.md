# MyMealsApp

# Kotlin Multiplatform Meals Application
This app generates a random list of meals categorized by their first letter upon each refresh with their respective images. Users will have the option to mark meals as favorites. The data for the app will be retrieved from TheMealDB API(www.themealdb.com/api/json/v1/1) and then cache it in our local database. Additionally, users can view their favorite meals, which are stored locally in our database.

<img width="1164" alt="android_ios_app" src="https://github.com/farahAtieh/MyMealsApp/assets/40662178/fa5c85fc-e797-4c62-880d-a299d9bd1442">

Every layer is shared except the UI.

### Tech stack
- Declarative UI with `Jetpack Compose` and `SwiftUI`
- [Koin](https://github.com/InsertKoinIO/koin)
- [Ktor](https://ktor.io/) (Backend + Client logic)
- [Kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization)
- [SqlDelight](https://github.com/cashapp/sqldelight)
- [BuildKonfig](https://github.com/yshrsmz/BuildKonfig)
- [KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)
- [google/ksp](https://github.com/google/ksp)

### Targets
- Android
- iOS
