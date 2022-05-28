package com.linx.applock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
    Setting up the applist, will display the list of applications that are installed on the working
    device
 */

public class AppList extends Fragment {
    // using a recycler view as it is more memory efficient, by only loading the list elements
    // which are near to the display
    private RecyclerView recyclerView;

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


    private void getPackages() {
        /*
        get a list of installed apps on the device and adds to a list of appCardStruct
        while resolving the app name and it's icons and maps it to the recyclerview

        ISSUE: only preinstalled apps are appearing for some reason, probably might have to fix permissions for the app
         */
        //List<ResolveInfo> appslist; // temporary list to get store all installed apps in it
        //List<appCardStruct> appsCard;   // extract for each app from appslist to create a list of apps based on our required structure

        AppSharedPref db = new AppSharedPref(getContext());

        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);

        List<AppCardStruct> appsCard = new ArrayList<>();
        for (ResolveInfo app : apps) {
            if (app.activityInfo.packageName.equals("com.linx.applock")) {
                continue;
            }

            AppCardStruct appCardInstance = new AppCardStruct();
            appCardInstance.setCardIcon(app.activityInfo.loadIcon(packageManager));
            appCardInstance.setCardPackageName(app.activityInfo.packageName);
            appCardInstance.setCardName((String) app.activityInfo.loadLabel(packageManager));
            if (db.containsEntry(appCardInstance.getCardPackageName())) {
                appCardInstance.setCardLockStatus(true);
            } else {
                appCardInstance.setCardLockStatus(false);
            }
            appsCard.add(appCardInstance);
        }

//            this.appsCard=apps.stream().sorted(Comparator.comparing(a -> a.activityInfo.loadLabel(packageManager).toString()))
//            .filter(a->!a.activityInfo.packageName.equals("com.linx.applock")).map(a->{
//                appCardStruct appCardInstance = new appCardStruct();
//                appCardInstance.setCardIcon(a.activityInfo.loadIcon(packageManager));
//                appCardInstance.setCardPackageName(a.activityInfo.packageName);
//                appCardInstance.setCardName((String) a.activityInfo.loadLabel(packageManager));
//                return appCardInstance;
//            }).peek(a->{
//                if (db.containsEntry(a.getCardPackageName())){
//                    a.setCardLockStatus(true);
//                }
//                else{
//                    a.setCardLockStatus(false);
//                }
//            }).collect(Collectors.toList());

        Collections.sort(appsCard, (a1, a2) -> a1.getCardName().toLowerCase().compareTo(a2.getCardName().toLowerCase()));
        AppListAdapter listAdapter = new AppListAdapter(appsCard);
        recyclerView.setAdapter(listAdapter);
    }
}