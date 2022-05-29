package com.linx.applock.SettingsTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linx.applock.R;
import com.linx.applock.SharedPrefsDB.SettingsSharedPref;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private List<SettingsCardStruct> settingChoices;
    private Context context;
    private SettingsSharedPref db;


    SettingsAdapter(List<SettingsCardStruct> lst) {
        this.settingChoices = lst;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.settingcardlayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        db = new SettingsSharedPref(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.settingTitle.setText(settingChoices.get(position).getSettingName());
        holder.checkBox.setChecked(settingChoices.get(position).getStatus());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            //adding event listener to toggle between enable/disable of checkbox
            @Override
            public void onClick(View v) {
                if (!settingChoices.get(position).getStatus()) {
                    settingChoices.get(position).setStatus(true);
                    holder.checkBox.setChecked(true);
                    db.enableSetting();
                } else {
                    settingChoices.get(position).setStatus(false);
                    holder.checkBox.setChecked(false);
                    db.disableSetting();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingChoices.size();
    }

    //binding the settingscardstruct to the layout so that values can be mapped to it
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView settingTitle;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            settingTitle = itemView.findViewById(R.id.settingnameid);
            checkBox = itemView.findViewById(R.id.settingCheckBox);
        }
    }
}
