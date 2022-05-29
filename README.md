# Applock - Face Recognition - Engage 2022
This is an Android Application developed for Microsoft Engage 2022. It is a Applock that takes advantage of the device biometrics provide Face Recognition and Fingerprint recognition to unlock apps. All this while using a very simple UI.

# Tech Stack
- **Java** - Written on android studio with java as the developing language
- **XML** - To design the UI of this program
- **Biometrics API** - to take advantage of the device's biometric capabilities to provide Face recognition
- **Shared Preferences** - Stores the apps that the appliction will lock

# Submissions
- Video - to be added
- PPT - to be added

# Using the app
- Download the app  here
- Make sure your device supports biometrics 
- Setup biometric face recognition (depending on the device you may need to also set up Fingerprint)

# Logic Design
- On launching the application the user is prompted with the biometric prompt which he must complete in order to gain access to the app.
- On successful authentication the user is redirected to a page .showing the list of apps that are installed on the device.
- The user can toggle to lock/unlock these apps.
- On locking, an entry for the app is created in the database (which is essentially keeping a record of all apps that the user wants to lock).
- On unlocking, the entry for that app is removed from the database.
- Using background services to monitor if a locked app is opened upon which authentication requirement is prompted.
- However the background service must be constantly restarted as Android automatically kills running background services.

# Issues

