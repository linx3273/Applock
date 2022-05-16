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
import java.util.Collections;
import java.util.Comparator;
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
        List<ResolveInfo> appslist; // temporary list to get store all installed apps in it
        List<appCardStruct> appsCard;   // extract for each app from appslist to create a list of apps based on our required structure

        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        appslist = packageManager.queryIntentActivities(intent, 0);
        appslist = sort(appslist, packageManager);
        appsCard = new ArrayList<>();

        for (ResolveInfo info: appslist){
            appCardStruct appCardInstance = new appCardStruct();
            appCardInstance.setCardIcon(info.activityInfo.loadIcon(packageManager));
            appCardInstance.setCardPackageName(info.activityInfo.packageName);
            appCardInstance.setCardName((String) info.activityInfo.loadLabel(packageManager));

            appsCard.add(appCardInstance);
        }

        AppListAdapter listAdapter = new AppListAdapter(appsCard);
        recyclerView.setAdapter(listAdapter);
    }



    private List<ResolveInfo> sort(List<ResolveInfo> appslist, PackageManager packageManager) {
        /*
        defined method to sort ResolveInfo based on name of app
        comparison is done on the name of app the compare() logic of which is based on the strCmp()
        function of C language
         */

        Collections.sort(appslist, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                /*
                Implementation of strcmp(a,b) of C language
                if
                    a > b returns +ve value
                    a < b returns -ve value
                    a = b returns 0
                 */

                // extracting name of app from each ResolveInfo
                String str1 = (String) o1.activityInfo.loadLabel(packageManager);
                String str2 = (String) o2.activityInfo.loadLabel(packageManager);

                // get length of each string
                int l1 = str1.length();
                int l2 = str2.length();

                for (int i = 0; i < Math.min(l1,l2); i++){
                    // obtain ASCII of character located at index i
                    int ch1 = (int) str1.charAt(i);
                    int ch2 = (int) str2.charAt(i);

                    if ( ch1 != ch2){
                        return ch1 - ch2;
                    }
                }

                if (l1 != l2){
                    return l1 - l2;     // when strings are equal upto lmin, used when strings are of unequal length
                }
                else{
                    return 0;   // when strings are equal
                }
            }
        });

        return appslist;
    }

}