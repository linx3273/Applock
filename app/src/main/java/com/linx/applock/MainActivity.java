package com.linx.applock;

import static android.app.AppOpsManager.MODE_ALLOWED;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.linx.applock.AppsTab.AppslistPage;
import com.linx.applock.Services.ApplockMonitor;
import com.linx.applock.SettingsTab.SettingsPage;
import com.linx.applock.authenticators.DeviceAuth;
import com.linx.applock.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private Fragment appsPage = new AppslistPage();
    private Fragment settingsPage = new SettingsPage();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        authCallback();
        setPermission();
        createFragments();

        //foreground service
        //checking if foreground service is already running to
        // ensure that multiple services are not created
        if (!foregroundServiceRunning()) {
            initiateService();
        }


        // event listener for the navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // creating an event listener for the options on the navigation bar
            // check the clicked menu item and call function accordingly using switch
            switch (item.getItemId()) {
                case R.id.applist:
                    getSupportFragmentManager().beginTransaction().show(appsPage).commit();
                    getSupportFragmentManager().beginTransaction().hide(settingsPage).commit();
                    break;

                case R.id.settings:
                    getSupportFragmentManager().beginTransaction().hide(appsPage).commit();
                    getSupportFragmentManager().beginTransaction().show(settingsPage).commit();
                    break;
            }
            return true;
        });

//        BackgroundManager.getInstance().init(this).startAlarmManager();
    }

    private void createFragments() {
        // generating fragments during app launch to prevent re construction on demand and thereby improve performance (tested and result improved from 500ms to 100ms
        // while consuming lesser memory)

        //reconstruction is prevented by hiding the fragments which prevents it from being destroyed
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, appsPage).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, settingsPage).commit();

        //setting appsPage as the default fragment on launching the app
        getSupportFragmentManager().beginTransaction().show(appsPage).commit();
        getSupportFragmentManager().beginTransaction().hide(settingsPage).commit();
    }

    private void authCallback() {
        // function launches an activity for biometric authentication and
        // waits for a response depending on authentication success or failure
        // incase the user tries to cancel the authentication status the application closes
        Intent intent = new Intent(this, DeviceAuth.class);

        ActivityResultLauncher<Intent> authResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_CANCELED) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                }
        );
        authResult.launch(intent);
    }

    public boolean getPermissionStatus(String permission) {
        Context context = getApplicationContext();
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());

        return mode == MODE_ALLOWED;
    }

    public void setPermission() {
        if (!getPermissionStatus(AppOpsManager.OPSTR_GET_USAGE_STATS)) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    public void initiateService() {
        Intent serviceIntent = new Intent(this, ApplockMonitor.class);
        startForegroundService(serviceIntent);
    }

    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (ApplockMonitor.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}