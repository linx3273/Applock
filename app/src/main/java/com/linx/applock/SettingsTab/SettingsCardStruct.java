package com.linx.applock.SettingsTab;

import android.widget.CheckBox;

import java.util.Objects;

public class SettingsCardStruct {
    /*
    a data class to bind to the settingscardlayout (frontend) with the backend with
    their respective getter and setter functions
     */
    private String settingName;
    private Boolean status;
    private CheckBox cbStatus;

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CheckBox getCbStatus() {
        return cbStatus;
    }

    public void setCbStatus(CheckBox cbStatus) {
        this.cbStatus = cbStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsCardStruct that = (SettingsCardStruct) o;
        return Objects.equals(settingName, that.settingName);
    }

}
