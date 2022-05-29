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
        editor.putString(String.valueOf(R.string.dbSettingId), "");
        editor.commit();
    }

    public void disableSetting() {
        if (shared.contains(String.valueOf(R.string.dbSettingId))) {
            editor.remove(String.valueOf(R.string.dbSettingId));
            editor.commit();
        }
    }

    public boolean isEnabled() {
        if (shared.contains(String.valueOf(R.string.dbSettingId)))
            return true;
        return false;
    }
}
