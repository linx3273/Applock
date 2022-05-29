package com.linx.applock.AppsTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.AppSharedPref;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    /*
    class to create an app card for adding as a display to the app during runtime
     */
    private List<AppCardStruct> installedApps;  // list of app cards that will be given to the recylerview
    private Context context;
    private AppSharedPref db;


    AppListAdapter(List<AppCardStruct> apps) {
        this.installedApps = apps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        // getting layout file to the adapter
        View view = LayoutInflater.from(context).inflate(R.layout.appcardlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        db = new AppSharedPref(parent.getContext());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        /*
        bind data to the card layout and add an event listener to check for toggles on
        locking/unlocking the app
         */
        holder.layoutIcon.setImageDrawable(installedApps.get(position).getCardIcon());
        holder.layoutName.setText(installedApps.get(position).getCardName());

        // creating the default lock status icon
        if (installedApps.get(position).isCardLockStatus()) {
            holder.lockstatus.setImageResource(R.drawable.lockedgreen);
        } else {
            holder.lockstatus.setImageResource(R.drawable.unlockedred);
        }

        // event listeners to check for toggles on the lock status of each app
        holder.lockstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // use click listeners to toggle between values and drawables
                if (!installedApps.get(position).isCardLockStatus()) {
                    installedApps.get(position).setCardLockStatus(true);
                    holder.lockstatus.setImageResource(R.drawable.lockedgreen);
                    db.addEntry(installedApps.get(position).getCardPackageName(), installedApps.get(position).getCardName());
                } else {
                    installedApps.get(position).setCardLockStatus(false);
                    holder.lockstatus.setImageResource(R.drawable.unlockedred);
                    db.removeEntry(installedApps.get(position).getCardPackageName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return installedApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView layoutIcon;
        TextView layoutName;
        ImageView lockstatus;

        public ViewHolder(@NonNull View itemView) {
            /*
            obtains an instance of the card layout to so that data can be bound to it
            */
            super(itemView);
            layoutName = itemView.findViewById(R.id.appnameid);
            layoutIcon = itemView.findViewById(R.id.appiconid);
            lockstatus = itemView.findViewById(R.id.lockstatusid);
        }
    }
}
