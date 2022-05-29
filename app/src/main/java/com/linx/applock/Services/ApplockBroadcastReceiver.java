package com.linx.applock.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ApplockBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // a receiver function, restarts the service when the device finishes rebooting
        // uses AndroidManifest BOOT_COMPLETE to receive message from the device that the boot has been completed
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, ApplockMonitor.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
