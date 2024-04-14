# easnx

A little util to easier life for Hzdr linux users

## configure

Save in your `~/.profile` (or everywhere else in order to have them as environment variables) the following:

```
HZDR_USER="myUser"; export HZDR_USER
HZDR_PASSWORD="myPwd"; export HZDR_PASSWORD
HZDR_SECRET_KEY="mySecretKey"; export HZDR_SECRET_KEY
```

Double quotes shall remain

To get a new the `HZDR_SECRET_KEY` you must destroy the current token and create a new one:
- login with `snx` as normal
- browse to https://www.hzdr.de/db/userdb.mydata.Manage2FA?pnid=
- delete and create token
- `SECRET` is the key you need
- don't close the page yet! You must save this new one on your phone auth app as well

## run it
```bash
cd easnx/
./gradlew run
```

You will see a tray icon where, other than "connect" and "disconnect", which are pretty self explanatory, you have also "copy TOTP", which will copy in your clipboard the current Hzdr TOTP, useful for logins where requested

The connection status will be visible by the tray icon color, green means it's connected, red disconnected. And it's live, it's gonna ping every second the secondary DNS, you can rely on that to get a quick verification of your VPN status

Note: `snx` experiences a segmentation fault sometimes. This happens when you don't get the green color after you clicked on "connect". In this case you have to reboot, I still didn't find a better solution for the meanwhile

## Known issues:

Tray menus don't work on Fedora 39 (KDE 5.115.0, Qt 5.15.12 with Wayland)

## credits:

- [Zoff](https://github.com/zoff99) with his [jni_notifications](https://github.com/zoff99/jni_notifications) for native nice-looking notifications

### original:

This is a Kotlin Multiplatform project targeting Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
