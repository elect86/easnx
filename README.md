# easnx

A little util to easier life for Hzdr linux users

## configure

Save in your `.profile` (or everywhere else in order to have them as environment variables) the following:

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

`./gradlew run`

You will see a tray icon where, other than "connect" and "disconnect", which are pretty self explanatory, you have also "copy TOTP", which will copy in your clipboard the current Hzdr TOTP, useful for logins where requested


### original:

This is a Kotlin Multiplatform project targeting Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
