# Vasco Android app

This app is used to receive intents broadcasted by the `adb`. A `BroadcastReceiver` grabs the intent, reads the coordinate, and sets the location accordingly using the `LocationManager`.

The app shows a button that when clicked will activate a custom location provider.

# Usage

1. Make sure to set `sdk.dir` with your Android sdk path in a `local.properties` file;

2. Run `./gradlew app:assembleDebug`;

3. Run `adb install -r app/build/outputs/apk/debug/app-debug.apk`;

4. On the Android phone go to `Settings` > `Developer options` and choose Vasco as `Mock location app`;

5. Open the app and click the button;

6. Connect your device to the computer and run `adb devices` to make sure your device is properly connected.

Now, you can check the app logs, using for instance the [Pidcat](https://github.com/JakeWharton/pidcat) tool, by running:

`pidcat com.ruigoncalo.vasco`

You should be able to see a `Location` object's data being printed every time you click the button.

## Mock locations from the adb

Run `adb shell am broadcast -a send.mock -e lat <lat> -e lon <lng>`

Open the Google maps app, for instance, to verify your locations.





