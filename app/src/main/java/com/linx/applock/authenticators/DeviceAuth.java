package com.linx.applock.authenticators;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.SettingsSharedPref;

import java.util.concurrent.Executor;

public class DeviceAuth extends AppCompatActivity {
    private static int authType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_auth);
        getAuthFormat();
        checkCompatibility();
        authenticate();
    }

    private void authenticate() {
        // creating the prompt to requesting the biometric
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication Required!")
                .setDescription("Complete authentication to gain access")
                .setAllowedAuthenticators(authType)
                .setConfirmationRequired(false)
                .build();
        // BIOMETRIC_WEAK - uses face || fingerprint || iris
        // BIOMETRIC_STRONG - uses only fingerprint
        // DEVICE_CREDENTIAL - requires pin/password/pattern based on what user has set up for his device

        //handling response to biometric authentications
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                setResult(RESULT_CANCELED);
                finish();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                // called when the user force closes the authentication error and it cannot be recovered
                super.onAuthenticationSucceeded(result);
                authSuccessful();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                // called when the the recognition is not occuring
                super.onAuthenticationFailed();
                authFailed();
            }
        });

        // calling the biometric authentication prompt based on the settings provided above
        biometricPrompt.authenticate(promptInfo);
    }

    private void checkCompatibility() {
        /*
        checks whether device has biometric support or not
         */
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(authType)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // device lacks the hardware to support biometric authentication on running device
                msgToast((int) R.string.hardwareMissing, Toast.LENGTH_LONG);
                // in these cases it will by default depend on DEVICE_CREDENTIALS (if set up)
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                //  although the hardware is present for biometric authentication, the hardware might have been disabled by the os or user
                msgToast((int) R.string.hardwareUnvailabale, Toast.LENGTH_LONG);
                // in these cases it will by default depend on DEVICE_CREDENTIALS (if set up)
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // user has not setup biometrics so prompt user to do that
                msgToast((int) R.string.biometricEnroll, Toast.LENGTH_LONG);
                startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                break;
        }
    }

    private void authFailed() {
        Toast.makeText(this, R.string.authfail, Toast.LENGTH_SHORT).show();
    }

    private boolean authSuccessful() {
        Toast.makeText(this, R.string.authsuccess, Toast.LENGTH_SHORT).show();
        return true;
    }


    private void msgToast(int msg, int size) {
        // a method to create custom Toast widget messages
        Toast.makeText(this, msg, size).show();
    }

    private void getAuthFormat() {
        //adjusts authenication prompt based on whether biometrics are enabled/disabled for the app
        SettingsSharedPref db = new SettingsSharedPref(getApplicationContext());
        if (db.isEnabled()) {
            authType = BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL;
        } else {
            authType = BiometricManager.Authenticators.DEVICE_CREDENTIAL;
        }

    }

}
