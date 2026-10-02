# Migration validation fixtures

`MigrationFixtureActivity` renders the production Compose screens with deterministic synthetic state. It does not read or modify the application's stored records, invoke repositories, or submit service actions. The debug manifest exports it only in debug builds; it is excluded from release builds.

Build with the project's normal command:

```sh
./gradlew :app:assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -W -n com.illusion.checkfirm/.MigrationFixtureActivity --es screen bookmark --es theme dark --ez populated true
```

Screen values: `home`, `firmware`, `search`, `bookmark`, `category`, `categoryedit`, `settings`, `welcome`, `catcher`, `manual`, `mydevice`, `network`, `bookmarkdialog`, `welcomedialog`, `catcherdialog`, `help`, `backup`, `about`, `report`, `sherlock`, `profile`, `theme`, `language`, `order`, `reset`, `legal`, and `contributor`.

Use `--ez populated false` for empty states. Populated fixtures include five devices, multiple categories, a long bookmark/profile name, report text, and official/test firmware histories. Selection and navigation-only controls can be exercised without database or service writes. Submission callbacks intentionally do nothing.

Two additional screens verify behavior without personal records:

- `storage` runs eight legacy identity, category assignment and delete assertions against disposable in-memory Room databases.
- `lifetime` uses the same Navigation 3 entry decorators as MainActivity. Open first, type a draft, open second, then return and pop. The first draft and ViewModel survive return; popped ViewModels clear, and reopening gets fresh state.

For XML comparisons, use an isolated reference package and equivalent data, configuration and settled capture timing. A successful build alone does not establish visual parity.
