package com.linx.applock.SharedPrefsDB;

import android.content.Context;
import android.content.SharedPreferences;

import com.linx.applock.R;

public class SettingsSharedPref {
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    public SettingsSharedPref(Context parentContext) {
        shared = parentContext.getSharedPreferences(String.valueOf(R.string.settingsSharedPrefName), Context.MODE_PRIVATE);

        editor = shared.edit();
    }

    public void enableSetting() {
        //enables biometric by writing to db
        editor.putString(String.valueOf(R.string.dbSettingId), "");
        editor.commit();
    }

    public void disableSetting() {
        //disables biometric by removing entry from db
        if (shared.contains(String.valueOf(R.string.dbSettingId))) {
            editor.remove(String.valueOf(R.string.dbSettingId));
            editor.commit();
        }
    }

    public boolean contains() {
        // checks for existence in db
        if (shared.contains(String.valueOf(R.string.dbSettingId)))
            return true;
        return false;
    }
}
