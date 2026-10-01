# Где отслеживать изменения Axion

Ветка исходного переноса: `lineage-23.2`. При переходе Axion на другую ветку
сначала проверьте совместимость API с текущей версией Evolution X.

## Базовые ревизии

- `android_frameworks_base`: `6b1b2e3a53b7c1e1b5602126d015357677a03457`.
- `android_packages_apps_AxionParts`: `5fe003cf19094512eddda60bdcf2ba5ba9147185`.

Это ревизии локальных исходников исходного переноса, а не указание, что они
являются последними версиями Axion. После обновления фиксируйте новые SHA.

## Движок

Репозиторий: https://github.com/AxionAOSP/android_frameworks_base

Основной путь:
`packages/SystemUI/src/com/android/systemui/routines/`.

| Путь относительно routines | Что проверять |
| --- | --- |
| `domain/trigger/` | Регистрация событий, время, геолокация, оценка триггеров |
| `domain/action/ActionExecutor.kt` | Выполнение Actions и новые зависимости |
| `domain/condition/` | Проверка условий |
| `domain/RoutinesInteractor.kt` | Запуск мониторинга и последовательность выполнения |
| `domain/RoutinesSettings.kt` | Ключи настроек и наблюдение за изменениями |
| `model/` | Типы Trigger, Condition, Action и Routine |
| `data/` | Хранение, сериализация, совместимость JSON |
| `ui/RoutinesManager.kt` | CoreStartable и инициализация движка |
| `ui/qs/` | QS-плитка, диалог, запуск вручную |

История:
https://github.com/AxionAOSP/android_frameworks_base/commits/lineage-23.2/packages/SystemUI/src/com/android/systemui/routines

## Интерфейс

Репозиторий: https://github.com/AxionAOSP/android_packages_apps_AxionParts

Основной путь:
`src/com/android/axion/axionparts/ui/screens/routines/`.

- `RoutinesScreen.kt`: список, включение, импорт и экспорт.
- `RoutineEditorScreen.kt`: редактор и параметры новых типов.
- `RoutineModels.kt`: модели UI.
- `RoutineSerializer.kt`: формат обмена с движком.
- Остальные файлы папки: компоненты и выбор приложений/параметров.
- `res/values/strings.xml` и переводы: ресурсы интерфейса.
- `Android.bp`, `AndroidManifest.xml`, `privapp-permissions-axionparts.xml`:
  изменения зависимостей и доступа к системе.

История:
https://github.com/AxionAOSP/android_packages_apps_AxionParts/commits/lineage-23.2/src/com/android/axion/axionparts/ui/screens/routines

## Интеграции за пределами routines

В `android_frameworks_base`:

- `packages/SystemUI/src/com/android/systemui/dagger/AxionStartableModule.kt`
  и активные Dagger-компоненты: регистрация CoreStartable.
- `packages/SystemUI/src/com/android/systemui/ax/`: AxPlatformStateManager,
  AxPlatformFeatureController, AxPlatformObservers, сервисы и стартовый модуль.
- `packages/SystemUI/Android.bp` и `AndroidManifest.xml`:
  библиотеки, разрешения и особенности сборки.
- `packages/SystemUI/res/`: связанные строки, конфигурация и значки.
- `core/res/res/values/`: системные ресурсы, включая слот значка.
- `libs/WindowManager/Shell/`: Bubbles API для соответствующего Action.
- Серверная часть AxPlatform и её запуск: при изменении client/callback API
  находите реализацию по именам AxPlatform и проверяйте обе стороны.

В организации Axion также проверяйте репозиторий, предоставляющий библиотеку
`ax_platform_hooks` и клиент `com.android.axion.platform`, если меняются
импорты, константы или callback API движка.

## Порядок ручного обновления

1. Сравнить изменения папок движка и интерфейса от зафиксированных SHA.
2. Согласованно перенести модели и оба сериализатора.
3. Проверить новые зависимости и интеграционные изменения вне этих папок.
4. Сохранить адаптации Evolution X, Activity и отключённые служебные toast/значок.
5. Выполнить сборку и проверить реальные триггеры на устройстве.
6. Записать новые SHA исходников.
