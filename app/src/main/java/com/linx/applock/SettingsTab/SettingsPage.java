package com.linx.applock.SettingsTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.SettingsSharedPref;

import java.util.ArrayList;
import java.util.List;

public class SettingsPage extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        recyclerView = rootView.findViewById(R.id.settings_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        generateSettings();
        return rootView;
    }

    public void generateSettings() {
        SettingsSharedPref db = new SettingsSharedPref(getContext());

        List<SettingsCardStruct> listOfSettings = new ArrayList<>();


        SettingsCardStruct settingInstance = new SettingsCardStruct();
        settingInstance.setSettingName("Enable biometrics");

        if (db.isEnabled()) {
            settingInstance.setStatus(true);
        } else {
            settingInstance.setStatus(false);

        }

        listOfSettings.add(settingInstance);
        SettingsAdapter listAdapter = new SettingsAdapter(listOfSettings);
        recyclerView.setAdapter(listAdapter);
    }
}