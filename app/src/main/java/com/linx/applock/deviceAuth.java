package com.linx.applock;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.concurrent.Executor;

public class deviceAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_auth);
        checkCompatibility();
        authenticate();
    }

    private int authenticate(){
        // creating the prompt to requesting the biometric
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication Required!")
                .setDescription("Complete authentication to gain access")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .setConfirmationRequired(false)
                .build();

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
                super.onAuthenticationSucceeded(result);
                authSuccessful();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authFailed();
            }
        });

        biometricPrompt.authenticate(promptInfo);
        return 1;
    }

    private void checkCompatibility(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgToast((int) R.string.hardwareMissing,Toast.LENGTH_LONG);
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgToast((int) R.string.hardwareUnvailabale, Toast.LENGTH_LONG);
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

    private void authFailed(){
        Toast.makeText(this, R.string.authfail,Toast.LENGTH_SHORT).show();
    }

    private boolean authSuccessful(){
        Toast.makeText(this, R.string.authsuccess, Toast.LENGTH_SHORT).show();
        return true;
    }


    private void msgToast(int msg, int size) {
        Toast.makeText(this, msg, size).show();
    }

}
