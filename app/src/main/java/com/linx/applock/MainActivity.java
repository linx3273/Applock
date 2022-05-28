package com.linx.applock;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.linx.applock.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Fragment appsPage = new AppList();
    private Fragment settingsPage = new Settings();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authCallback();

        // setting applist as the default fragment when the application is launched
        replaceFragment(appsPage);

        // event listener for the navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // creating an event listener for the options on the navigation bar
            // check the clicked menu item and call function accordingly using switch
            switch (item.getItemId()) {
                case R.id.applist:
                    replaceFragment(appsPage);
                    break;

                case R.id.settings:
                    replaceFragment(settingsPage);
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        // switches from the current frame to the frame provided in the argument
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

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
}