package com.example.uasiot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uasiot.Model.WaterflowModel;
import com.example.uasiot.R;

import java.util.List;

public class WaterflowAdapter extends RecyclerView.Adapter<WaterflowAdapter.ViewHolder> {

    private List<WaterflowModel> waterflowList;

    public WaterflowAdapter(List<WaterflowModel> waterflowList) {
        this.waterflowList = waterflowList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waterflow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WaterflowModel waterflow = waterflowList.get(position);
        holder.Countername.setText(waterflow.getNama());
        holder.rateTextView.setText("Rate: " + String.valueOf(waterflow.getRate()) + " L/m");
        holder.volumeTextView.setText("Volume: " + String.valueOf(waterflow.getVolume_L()) + " L");
    }

    @Override
    public int getItemCount() {
        return waterflowList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rateTextView;
        TextView volumeTextView;
        TextView Countername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rateTextView = itemView.findViewById(R.id.ratecount);
            volumeTextView = itemView.findViewById(R.id.volume);
            Countername = itemView.findViewById(R.id.textRate);
        }
    }
}
