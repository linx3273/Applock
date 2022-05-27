package com.linx.applock;

import android.content.Context;
import android.content.SharedPreferences;


public class appSharedPref {
    Context context;
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    public appSharedPref(Context parentContext) {
        context = parentContext;
        shared = context.getSharedPreferences(String.valueOf(R.string.sharedPrefName), Context.MODE_PRIVATE);
        editor = shared.edit();
    }


    public void addEntry(String packageName, String appName){
        // adds an entry to the database consisting of key-value pairs
        // key - packageName
        // value is not required as being present in the database means that the application is locked
        editor.putString(packageName,"");
        editor.commit();
    }

    public void removeEntry(String packageName){
        // deletes entry from database
        if(shared.contains(packageName)){
            editor.remove(packageName);
            editor.commit();
        }
    }

    public boolean containsEntry(String packageName){
        // returns true of a given package name is stored in the database
        if(shared.contains(packageName))
            return true;
        return false;
    }

}
