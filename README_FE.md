StandardApp - Frontend demo

What this implements:
- Kotlin core: `data class` (Item), `enum class` (ItemType), `sealed class` (UiState), `companion object` used in adapters example
- Naming conventions: follow Android/Kotlin standard packages and PascalCase for classes
- Android lifecycle: `MainActivity`, `Fragment` lifecycle via `HomeFragment`/`DetailFragment`
- Single Activity / multiple Fragments: `MainActivity` hosts `NavHostFragment` with navigation graph
- Fragment lifecycle: view creation and viewLifecycleOwner usage in `HomeFragment`
- ViewModel: `HomeViewModel` using Hilt injection
- Navigation: `nav_graph.xml` with actions
- DialogFragment: `SampleDialogFragment`
- XML UI & Custom XML: layouts in `res/layout` demonstrate usage
- ViewPager2: included in `fragment_home.xml` as `ViewPager2`
- NestedScrollView: used in `fragment_home.xml`
- ImageFilterView: used in `fragment_home.xml`
- Aspect Ratio / PreviewView / TextureView: placeholders included in `fragment_home.xml`
- Permissions: CAMERA permission must be requested at runtime when adding camera logic
- Parse JSON from raw: `mock_data.json` parsed by `MockRepository` using Gson
- SharedPreferences: add or inspect `Settings` usage placeholder
- Coroutines & Flow: `HomeViewModel` uses coroutines and `StateFlow`
- Dagger Hilt: App annotated with `@HiltAndroidApp`, DI module provides repository

How to run:
- Open the project in Android Studio
- Let Gradle sync (we added dependencies in the version catalog)
- Build and run on a device or emulator (minSdk 24)

Next steps (optional):
- Implement RecyclerView adapter for item list with click navigation
- Add runtime permission flow for camera and implement CameraX preview using `PreviewView`
- Integrate Retrofit when backend API is available

Files of interest:
- app/src/main/res/raw/mock_data.json
- app/src/main/java/com/example/standardapp/data/MockRepository.kt
- app/src/main/java/com/example/standardapp/ui/home/HomeViewModel.kt
- app/src/main/java/com/example/standardapp/ui/home/HomeFragment.kt

If you want, I can now implement a RecyclerView adapter, add camera permission handling, or wire up ViewPager content. Which should I do next?
