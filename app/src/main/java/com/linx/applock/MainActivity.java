package com.linx.applock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.linx.applock.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setting applist as the default fragment when the application is launched
        replaceFragment(new applist());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // creating an event listener for the options on the navigation bar
            // check the clicked menu item and call function accordingly using switch
            switch (item.getItemId()) {
                case R.id.applist:
                    replaceFragment(new applist());
                    break;

                case R.id.settings:
                    replaceFragment(new settings());
                    break;
            }

            return true;
        });
    }

    private  void replaceFragment(Fragment fragment){
        // switches from the current frame to the frame provided in the argument
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
}