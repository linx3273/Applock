package com.linx.applock.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.AppSharedPref;
import com.linx.applock.SharedPrefsDB.SettingsSharedPref;

import java.util.concurrent.Executor;

public class ApplockMonitor extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //maintaining the foreground service even after the app is terminated and generating a notification to alert the user
        startForeground(3273, createNotification().build());

        // initiating a new thread for the service that were regularly check for the running application
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {


                        Looper.prepare();

                        while (true) {
                            /*
                             TODO
                              I have spent hours on trying to figure this out and looked over countless guides but none are giving me the result I require
                              as most of these guides are almost 12 years old and several functionalities that they use are now deprecated making it really hard
                              for me to figure out how to go about.
                              Also, after android 10 services are no longer allowed to call activities due to which calling Authenticator for other apps would
                              probably require a very  complex roundabout implementation.
                              Time constraints will force me to drop this as for now, I'll mostly work on it after the submission/evaluation.
                             */
                            if (existsinDB()) {
                                Toast.makeText(getApplicationContext(), "Failed to intialize authenticator", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        ).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public Notification.Builder createNotification() {
        // creating a non-destructible notification bubble to let the user know that there's
        // a background service that is running the device
        // and returning the notifcation instance so that it can be bound to the service
        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Applock service is running")
                .setContentTitle("Applock")
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        return notification;
    }


    public String getLauncherTopApp() {
        // requires ACTION_USAGE_ACCESS_SETTINGS permission to monitor the usage times of applications
        // Android Lollipop (API level 22) was the last android in which running app info was given willingly to
        // third party apps...i.e. any later android version will not provide the running apps that are not signed or system

        // This is a round about method were get the lastest running app and collect system times to
        // check when it was launched

        // collect the most recently launched app in the last 5 seconds and obtain its packageName
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(this.USAGE_STATS_SERVICE);

        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 5; //5 milliseconds

        String result = "";

        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName();
            }
        }

        if (!TextUtils.isEmpty(result)) {
            return result;
        }

        return "";
    }

    public boolean existsinDB() {
        // function returns true/false based on whether the packageName obtained by getLauncherTopApp()
        // is present in the database or not
        // if present return true - to initiate the authorization request
        // else false and do nothing in that case
        AppSharedPref dbapps = new AppSharedPref(this);

        if (dbapps.containsEntry(getLauncherTopApp())) {
            return true;
        }
        return false;
    }


    public void auth() {
        // currently unused but aim was to call on a authenticator if the obtained package from
        // getLauncherTopApp however services cannot call another activity so i'll have to look for
        // another solution

        int authType;
        SettingsSharedPref dbset = new SettingsSharedPref(this);
        if (!dbset.contains()) {
            authType = BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL;
        } else {
            authType = BiometricManager.Authenticators.DEVICE_CREDENTIAL;
        }


        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication Required!")
                .setDescription("Complete authentication to gain access")
                .setAllowedAuthenticators(authType)
                .setConfirmationRequired(false)
                .build();

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) this.getApplicationContext(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }
}

