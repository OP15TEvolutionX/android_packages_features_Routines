# Routines for Evolution X

Исходники автоматизации AxionOS, адаптированные для Evolution X.
Движок компилируется и работает внутри SystemUI; экран настроек — внутри
com.android.settings. Этот репозиторий не является отдельным APK.

## Структура

- `systemui/src/`: модели, хранилище, движок, триггеры, Actions и QS-плитка.
- `systemui/res/`: строки и значок QS-плитки.
- `settings/src/`: интерфейс, редактор, импорт/экспорт и Settings Activity.
- `settings/res/`: строки интерфейса и значок пункта Evolver.
- `Android.bp`: два Soong filegroup для сборки в модулях-хозяевах.
- [UPSTREAM.md](UPSTREAM.md): где проверять изменения Axion.

## Подключение

Manifest: проект `android_packages_features_Routines`, ветка `cnb`, путь
`packages/features/Routines`. Внутри project нужны linkfile:

```xml
<linkfile src="systemui/res" dest="frameworks/base/packages/SystemUI/routines-res" />
<linkfile src="settings/res" dest="packages/apps/Settings/routines-res" />
```

Эти ссылки создаёт `repo sync`; не коммитьте их в репозитории-хозяева.

1. `frameworks/base/packages/SystemUI/Android.bp`:
   `SystemUI-core.srcs` включает `:evolution-routines-systemui-srcs`;
   `SystemUI-res.resource_dirs` включает `routines-res`.
2. `packages/apps/Settings/Android.bp`:
   `Settings-core.srcs` включает `:evolution-routines-settings-srcs`;
   `resource_dirs` включает `routines-res`.
3. Общий `SystemUIModule` включает
   `com.android.systemui.routines.dagger.RoutinesModule.class`.
   Регистрация должна попасть в фактический компонент SystemUIGoogle.
4. `Settings/AndroidManifest.xml` регистрирует
   `org.evolution.settings.fragments.routines.RoutinesActivity`,
   `exported=false`, с темой `Theme.Settings.NoActionBar`.
5. Пункт `routines` в `Evolver/res/xml/evolution_settings.xml` использует
   `@drawable/ic_routines` и explicit intent на эту Activity в
   `com.android.settings`. Он не открывает SubSettings-фрагмент.

## Системные зависимости

AxPlatform client/hooks и контроллеры `com.android.systemui.ax` должны быть
подключены и запущены в SystemUI. Код использует внутренние API SystemUI,
SettingsLib, Compose, Dagger, coroutines и WindowManager Shell.
Сохранены исходные пространства имён, чтобы ограничить отличия от Axion.

Также нужны интеграционные изменения исходного переноса:

- Разрешения SystemUI: ACCESS_FINE_LOCATION, READ_PHONE_STATE, READ_CALL_LOG,
  RECEIVE_SMS и SEND_SMS. USE_EXACT_ALARM уже предоставлен SystemUI.
- API `Bubbles.showAppBubble` и реализация в BubbleController для Action пузырей.
- Серверная интеграция AxPlatform.

Существующий слот значка Routines можно сохранить для совместимости, но этот
порт не создаёт значок в статусной строке. RoutinesManager по-прежнему запускает
interactor.init(). Toast о запуске routine отключён. Явные Actions уведомления
и сообщения, а также сообщения об ошибках импорта/экспорта сохранены.

## Отличия от Axion

- Ключи `evo_routines_*`, чтение legacy-ключей `ax_routines_*`.
- Адаптация к текущему AxPlatform API Evolution X.
- UI в пакете Evolver и ресурсы `com.android.settings.R`.
- Отдельная полноэкранная Activity: системные динамические цвета SettingsTheme,
  тема Activity выбирается через тот же SettingsThemeHelper, что и в Settings;
  edge-to-edge, возврат непосредственно к вызывающему экрану.
- Отдельный значок автоматизации в меню Evolver.
- Выполнение без автоматического toast и значка статусной строки.
- Собственный небольшой RoutinesModule для Dagger и QS.

Скрипта импорта нет. При обновлении вручную сверяйте движок, UI, формат JSON
и изменения зависимостей по UPSTREAM.md.

## Проверка интеграции

```sh
source build/envsetup.sh
lunch evolution_fairlady-userdebug
m evolution
```

После изменений Dagger проверяйте в сгенерированном Google-компоненте наличие
`RoutinesManager.class` в карте CoreStartable и `routines` в карте QS-плиток.
На устройстве проверьте открытие из Evolver, светлую/тёмную тему, редактор,
возврат назад и реальные триггеры. Успешная сборка не заменяет эту проверку.

## Лицензия

Исходники AxionOS сохраняют исходные copyright-заголовки. Код распространяется
под Apache License 2.0; см. [LICENSE](LICENSE).
