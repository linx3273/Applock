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

import androidx.annotation.Nullable;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.AppSharedPref;

public class ApplockMonitor extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //maintaining the foreground service even after the app is terminated and generating a notification to alert the user
        startForeground(3273, createNotification().build());

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        while (existsinDB()) {
                            /*
                             TODO I have spent hours on trying to figure this out and looked over countless guides but none are giving me the result I require
                             TODO as most of these guides are almost 12 years old and several functionalities that they use are now depracated making it really hard
                             TODO for me to figure out how to go about.
                             TODO time constraints will force me to drop this as for now, I'll mostly work on it after the submission/evaluation.

                             */
//                            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService("activity");
//                            activityManager.killBackgroundProcesses();

//                            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
//                            launchIntent.addCategory(Intent.CATEGORY_HOME);
//                            ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//
//                            ActivityInfo activityInfo = resolveInfo.activityInfo;
//                            ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                            startActivity(intent);
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

        //creating a notification to prompt the user that a service is running
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
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(this.USAGE_STATS_SERVICE);

        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10;

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
        AppSharedPref dbapps = new AppSharedPref(this);

        if (dbapps.containsEntry(getLauncherTopApp())) {
            return true;
        }
        return false;

    }
}
