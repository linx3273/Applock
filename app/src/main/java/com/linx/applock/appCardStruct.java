package com.linx.applock;

import android.graphics.drawable.Drawable;


public class appCardStruct {
    /*
    class designed for matching the structure of the layout @res/layout/appcardlayout.xml
    Each of the variables have a getter and setter function defined to map data to them
     */
    String cardPackageName; // package name format of app eg com.abc.def
    Drawable cardIcon;  //icon of the app
    String cardName;    // name of the app
    boolean cardLockStatus; // store the current lock status of the app

    @Override
    public boolean equals(Object o) {
        /*
        The equals() method is used to compare a given string to the specified object.
        The result is true if and only if the argument is not null and is a String object that
        represents the same sequence of characters as this object.
         */
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
