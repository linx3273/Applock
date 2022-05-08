package com.linx.applock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/*
    Setting up the applist, will display the list of applications that are installed on the working
    device
 */

public class applist extends Fragment {
    // using a recycler view as it is more memory efficient, by only loading the list elements
    // which are near to the display
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_applist, container, false);
        recyclerView = rootView.findViewById(R.id.app_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPackages();

        return rootView;
    }

    public void getPackages(){
        /*
        get a list of installed apps on the device and adds to a list of appCardStruct
        while resolving the app name and it's icons and maps it to the recyclerview

        ISSUE: only preinstalled apps are appearing for some reason, probably might have to fix permissions for the app
         */
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appslist = packageManager.queryIntentActivities(intent, 0);
        List<appCardStruct> apps = new ArrayList<>();

        for (ResolveInfo info: appslist){
            appCardStruct appCardInstance = new appCardStruct();
            appCardInstance.setCardIcon(info.activityInfo.loadIcon(packageManager));
            appCardInstance.setCardPackageName(info.activityInfo.packageName);
            appCardInstance.setCardName((String) info.activityInfo.loadLabel(packageManager));

            apps.add(appCardInstance);
        }

        AppListAdapter listAdapter = new AppListAdapter(apps);
        recyclerView.setAdapter(listAdapter);
    }
}