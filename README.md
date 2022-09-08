# Applock - Face Recognition - Engage 2022
This is an Android Application developed for Microsoft Engage 2022. It is a Applock that takes advantage of the device biometrics provide Face Recognition and Fingerprint recognition to unlock apps. All this while using a very simple UI.

# Tech Stack
- **Java** - Written on android studio with java as the developing language.
- **XML** - To design the UI of this program.
- **Biometrics API** - to take advantage of the device's biometric capabilities to provide Face recognition.
- **Shared Preferences** - Stores the apps that the appliction will lock and also used to save settings.

# Tested Devices
- Samsung devices (running android 12) supported with both fingerprint and Face unlock set up.
- REALME (running android 11) required removal of fingerprint authentication to use Face unlock.

# Issues
- Authenticator for apps besides the application itself is not working, Referred to guides but most of the methods are deprecated (I will remove this part of the README if I'm able to fix this issue).
