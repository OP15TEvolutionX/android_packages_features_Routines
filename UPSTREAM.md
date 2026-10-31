# Tracking upstream Axion changes

The original port uses the `lineage-23.2` branch. If Axion moves to another
branch, check API compatibility with the current Evolution X version first.

## Baseline revisions

- `android_frameworks_base`: `6b1b2e3a53b7c1e1b5602126d015357677a03457`.
- `android_packages_apps_AxionParts`: `5fe003cf19094512eddda60bdcf2ba5ba9147185`.

These revisions identify the local sources used for the original port. They do
not imply that these are the latest Axion revisions. Record new commit hashes
after each update.

## Engine

Repository: https://github.com/AxionAOSP/android_frameworks_base

Main path: `packages/SystemUI/src/com/android/systemui/routines/`.

| Path relative to routines | What to review |
| --- | --- |
| `domain/trigger/` | Event registration, time, location and trigger evaluation |
| `domain/action/ActionExecutor.kt` | Action execution and new dependencies |
| `domain/condition/` | Condition evaluation |
| `domain/RoutinesInteractor.kt` | Monitor startup and execution order |
| `domain/RoutinesSettings.kt` | Settings keys and change observers |
| `model/` | Trigger, Condition, Action and Routine types |
| `data/` | Storage, serialization and JSON compatibility |
| `ui/RoutinesManager.kt` | CoreStartable and engine initialization |
| `ui/qs/` | Quick Settings tile, dialog and manual execution |

History:
https://github.com/AxionAOSP/android_frameworks_base/commits/lineage-23.2/packages/SystemUI/src/com/android/systemui/routines

## User interface

Repository: https://github.com/AxionAOSP/android_packages_apps_AxionParts

Main path: `src/com/android/axion/axionparts/ui/screens/routines/`.

- `RoutinesScreen.kt`: routine list, enable switch, import and export.
- `RoutineEditorScreen.kt`: editor and options for new types.
- `RoutineModels.kt`: UI models.
- `RoutineSerializer.kt`: data exchange format used by the engine.
- Other files in the directory: components and app or option pickers.
- `res/values/strings.xml` and translations: UI resources.
- `Android.bp`, `AndroidManifest.xml`, `privapp-permissions-axionparts.xml`:
  dependency and system access changes.

History:
https://github.com/AxionAOSP/android_packages_apps_AxionParts/commits/lineage-23.2/src/com/android/axion/axionparts/ui/screens/routines

## Integrations outside routines

In `android_frameworks_base`, review:

- `packages/SystemUI/src/com/android/systemui/dagger/AxionStartableModule.kt`
  and active Dagger components: CoreStartable registration.
- `packages/SystemUI/src/com/android/systemui/ax/`: AxPlatformStateManager,
  AxPlatformFeatureController, AxPlatformObservers, services and startup module.
- `packages/SystemUI/Android.bp` and `AndroidManifest.xml`:
  libraries, permissions and build configuration.
- `packages/SystemUI/res/`: related strings, configuration and icons.
- `core/res/res/values/`: system resources, including the status bar icon slot.
- `libs/WindowManager/Shell/`: Bubbles API used by the corresponding action.
- AxPlatform server implementation and startup: when client or callback APIs
  change, find implementations by their AxPlatform names and check both sides.

Also check the repository in the Axion organization that provides
`ax_platform_hooks` and the `com.android.axion.platform` client when engine
imports, constants or callback APIs change.

## Manual update procedure

1. Compare engine and UI directory changes against the recorded commit hashes.
2. Update the models and both serializers together.
3. Review new dependencies and integration changes outside these directories.
4. Preserve Evolution X adaptations, the Activity and disabled automatic
   execution toasts and status bar icon.
5. Build the ROM and test actual triggers on the device.
6. Record the new upstream commit hashes.
