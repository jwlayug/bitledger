This directory contains the unzipped output of the settings.jar file that is created when clicking File > Export Settings... in IDEA [1]. This is done in order to allow these settings to be easily shared and version controlled.

To import these shared settings:

 1. run `./gradlew ideaSettingsJar` from the root of the repository. This will produce a file at build/libs/idea-settings.jar.
 2. From within IDEA, click File > Import Settings... and point to this file.
 3. Verify that the import was successful by going to Preferences > Code Style, where you should now see "Bitledger" as the active scheme.

To re-export these settings (i.e. when changes are made that should be shared):

 1. From within IDEA, click File > Export Settings...
 2. Click "Select None"
 3. Check the following settings to export:
   - Code style schemes
   - Inspection profiles
 4. In the "Export settings to" field, leave the default of the project root in place
 5. From within the .idea/exported_settings directory of the project run `jar -xvf ../../settings.jar`
 6. Compare differences using `git diff`. Be sure there aren't any system-specific file paths!
 7. Commit the changes

[1]: http://confluence.jetbrains.com/display/IntelliJIDEA/Shared+Preferences
