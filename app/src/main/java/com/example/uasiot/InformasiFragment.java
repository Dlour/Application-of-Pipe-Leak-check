package com.example.uasiot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uasiot.Model.WaterflowModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InformasiFragment extends Fragment {

    private TextView rateTextView;
    private TextView volumeTextView;

    public InformasiFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informasi, container, false);

        rateTextView = view.findViewById(R.id.count);
        volumeTextView = view.findViewById(R.id.countVol);

        fetchWaterflowData();

        return view;
    }

    private void fetchWaterflowData() {
        FirebaseDatabase.getInstance().getReference("allCounter/counter_b")
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
