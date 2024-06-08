package com.example.uasiot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uasiot.Model.WaterflowModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TagihanFragment extends Fragment {

    public TagihanFragment() {
        // Required empty public constructor
    }
    private TextView rateTextView;
    private TextView volumeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        fetchWaterflowData();
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_tagihan, container, false);
        rateTextView = view.findViewById(R.id.count);
        volumeTextView = view.findViewById(R.id.countVol);
         return view;
    }
    private void fetchWaterflowData() {
        FirebaseDatabase.getInstance().getReference("allCounter/counter_a")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            WaterflowModel waterflow = snapshot.getValue(WaterflowModel.class);
                            if (waterflow != null) {
                                rateTextView.setText(String.valueOf(waterflow.getRate()) + " L/m");
                                volumeTextView.setText(String.valueOf(waterflow.getVolume_L()) + " L");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
    }
}