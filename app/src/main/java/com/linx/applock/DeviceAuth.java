package com.linx.applock;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class DeviceAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_auth);
        checkCompatibility();
        authenticate();
    }

    private void authenticate() {
        // creating the prompt to requesting the biometric
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication Required!")
                .setDescription("Complete authentication to gain access")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
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
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
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
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL);

                ActivityResultLauncher<Intent> enrollBiometric = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {

                            }
                        }
                );
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

}
