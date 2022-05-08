package com.linx.applock;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    List<appCardStruct> installedApps;
    Context context;

    AppListAdapter(List<appCardStruct> apps){
        this.installedApps = apps;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }
        // getting layout file to the adapter
        View view = LayoutInflater.from(context).inflate(R.layout.appcardlayout,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layoutIcon.setImageDrawable(installedApps.get(position).cardIcon);
        holder.layoutName.setText(installedApps.get(position).cardName);

        if(installedApps.get(position).cardLockStatus){
            holder.lockstatus.setImageResource(R.drawable.lockedgreen);
        }
        else{
            holder.lockstatus.setImageResource(R.drawable.unlockedred);
        }

        holder.lockstatus.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (installedApps.get(position).cardLockStatus){
                    installedApps.get(position).cardLockStatus = false;
                    holder.lockstatus.setImageResource(R.drawable.lockedgreen);
                }
                else{
                    installedApps.get(position).cardLockStatus = true;
                    holder.lockstatus.setImageResource(R.drawable.unlockedred);

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
            super(itemView);
            layoutName = itemView.findViewById(R.id.appnameid);
            layoutIcon = itemView.findViewById(R.id.appiconid);
            lockstatus = itemView.findViewById(R.id.lockstatusid);
        }
    }
}
