package com.linx.applock;

import android.graphics.drawable.Drawable;

public class appCardStruct {
    String cardPackageName;
    Drawable cardIcon;
    String cardName;
    boolean cardLockStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        appCardStruct that = (appCardStruct) o;
        return cardPackageName.equals(that.cardPackageName);
    }

    public String getCardPackageName() {
        return cardPackageName;
    }

    public void setCardPackageName(String cardPackageName) {
        this.cardPackageName = cardPackageName;
    }

    public Drawable getCardIcon() {
        return cardIcon;
    }

    public void setCardIcon(Drawable cardIcon) {
        this.cardIcon = cardIcon;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public boolean isCardLockStatus() {
        return cardLockStatus;
    }

    public void setCardLockStatus(boolean cardLockStatus) {
        this.cardLockStatus = cardLockStatus;
    }
}
