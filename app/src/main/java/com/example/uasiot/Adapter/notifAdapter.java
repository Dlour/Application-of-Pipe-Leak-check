package com.example.uasiot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uasiot.Model.notifModel;
import com.example.uasiot.R;

import java.util.List;

public class notifAdapter extends RecyclerView.Adapter<notifAdapter.NotifViewHolder> {

    private List<notifModel> notifList;
    private Context context;

    public notifAdapter(Context context, List<notifModel> notifList) {
        this.context = context;
        this.notifList = notifList;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_item, parent, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        notifModel notif = notifList.get(position);
        holder.time.setText(notif.getTimestamp());
        holder.textNotif.setText(notif.getMessage());

        if (notif.isRead()) {
            holder.textNotif.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_regular));
            holder.circlenotif.setVisibility(View.GONE);
        } else {
            holder.textNotif.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_medium));
            holder.circlenotif.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public static class NotifViewHolder extends RecyclerView.ViewHolder {

        public ImageView gambarnotif, circlenotif;
        public TextView textNotif, time;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarnotif = itemView.findViewById(R.id.gambarnotif);
            circlenotif = itemView.findViewById(R.id.circlenotif);
            textNotif = itemView.findViewById(R.id.textNotif);
            time = itemView.findViewById(R.id.time);
        }
    }
}
