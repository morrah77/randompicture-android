#Random picture Android application

Displays a random picture fetched from backend server.

Refreshes a picture when tapped.

##Current status:

available for internal testing

##App details
  - App name: Random Picture
  - Short description: Shows random picture, refreshes the picture by user request
  - Full description: Got bored? Grab a random image to look at, probably, it entertains you!

##Development

###Build

####Prerequisites

Install gradle-wrapper.jar into `gradle/wrapper` directory (see version to download in [gradle/wrapper/gragle-wrapper.properties](gradle/wrapper/gragle-wrapper.properties)); create a symlink if you already have gradle-wrapper.jar installed

Create your own .properties files:

```
cp local.properties.template local.properties

cp secrets/secrets.properties.template secrets/secrets.properties
```

In `local.properties` file specify your own path to Android SDK to be used by Gradle.

###Build application

```
gradle build
```

###Test

```
./gradlew test

./gradlew connectedAndroidTest
```

###Run

####Prerequisites

Run local backend server

```
docker run --rm --name random_pict_nginx -p 8089:80 -v `pwd`/dev_env/docker/backend/nginx.conf:/etc/nginx/conf.d/default.conf -v `pwd`/dev_env/docker/backend/images:/usr/share/nginx/html/images nginx
```

####Run the application

Use Android Emulator or run `./gradlew assembleDebug`, command upload built `app/build/outputs/apk/debug/app-debug.apk` file to your test device, install and run the application.


###Release

####Prerequisites

To be able to release this application, you're expected to have an access to appropriate Google Play Console account, to have an upload key along with its credentials and a secrets/secrets.properties file containing some release-related values like production backend server URL, release version etc.

Bump (increase) versionCode and versionName in secrets/secrets.properties file.

Build signed application bndle for `release` build type:

```
./gradlew cleanBuildCache clean
./gradlew signReleaseBundle

```

Now Gradle does not sign the bundle so use standard Java jarsigner tool

```
jarsigner -keystore keys/keystore.jks -storepass $KEYSTORE_PASS -keypass $KEYSTORE_PASS app/build/outputs/bundle/release/app-release.aab random_picture_key

jarsigner -verify -keystore keys/keystore.jks -storepass $KEYSTORE_PASS -keypass $KEYSTORE_PASS app/build/outputs/bundle/release/app-release.aab random_picture_key
```

Fllow Google Play console instrucrions to create and rollout new release.
