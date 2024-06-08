package com.example.uasiot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uasiot.Adapter.WaterflowAdapter;
import com.example.uasiot.Model.WaterflowModel;
import com.example.uasiot.Model.logicModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AllFragment extends Fragment {

    private RecyclerView recyclerView;
    private WaterflowAdapter adapter;
    private List<WaterflowModel> waterflowList;
    private List<logicModel> logicModelList;

    private FirebaseFirestore firestore;

    private double countA, countB, countC;
    private static final String CHANNEL_ID = "UAS_IOT_CHANNEL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        waterflowList = new ArrayList<>();
        adapter = new WaterflowAdapter(waterflowList);
        recyclerView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();

        fetchWaterflowData();
        fetchCounterData();

        createNotificationChannel(); // Panggil di sini untuk memastikan channel dibuat saat fragment dibuat

        return view;
    }

    private void fetchCounterData() {
        fetchAndStoreCounter("allCounter/counter_a/rate", "counter_a");
        fetchAndStoreCounter("allCounter/counter_b/rate", "counter_b");
        fetchAndStoreCounter("allCounter/counter_c/rate", "counter_c");
    }

    private void fetchAndStoreCounter(String counterPath, final String counterKey) {
        FirebaseDatabase.getInstance().getReference(counterPath)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Double counterValue = dataSnapshot.getValue(Double.class);
                            if (counterValue != null) {
                                storeCounterInFirestore(counterKey, counterValue);
                                updateRates(counterKey, counterValue);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    private void storeCounterInFirestore(String counterKey, Double counterValue) {
        Map<String, Object> counterData = new HashMap<>();
        counterData.put("rate", counterValue);

        firestore.collection("counters").document(counterKey)
                .set(counterData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully written
                })
                .addOnFailureListener(e -> {
                    // Handle possible errors.
                });
    }

    private void fetchWaterflowData() {
        FirebaseDatabase.getInstance().getReference("allCounter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        waterflowList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot counterSnapshot : snapshot.getChildren()) {
                                WaterflowModel waterflow = counterSnapshot.getValue(WaterflowModel.class);
                                if (waterflow != null) {
                                    waterflowList.add(waterflow);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
    }

    private void updateRates(String counterKey, double counterValue) {
        if ("counter_a".equals(counterKey)) {
            countA = counterValue;
        } else if ("counter_b".equals(counterKey)) {
            countB = counterValue;
        } else if ("counter_c".equals(counterKey)) {
            countC = counterValue;
        }

        double sumRateAB = countA + countB;
        storeSumRateInFirestore(sumRateAB);
        if (sumRateAB > countC) {
            sendNotification();
        }
    }

    private void storeSumRateInFirestore(double sumRateAB) {
        Map<String, Object> sumData = new HashMap<>();
        sumData.put("sum_rate_ab", sumRateAB);

        firestore.collection("counters").document("sum_rate_ab")
                .set(sumData)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully written
                })
                .addOnFailureListener(e -> {
                    // Handle possible errors.
                });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "UAS IoT Channel";
            String description = "Channel for UAS IoT notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("AWASS!!")
                .setContentText("Terjadi Kebocoran Pipa!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());

        // Simpan informasi notifikasi ke Firestore
        saveNotificationToFirestore();
    }

    private void saveNotificationToFirestore() {
        // Dapatkan waktu dan tanggal saat ini
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(date);
        String message = "Terjadi Kebocoran Pipa!!";

        // Buat objek data notifikasi
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("timestamp", formattedDate);
        notificationData.put("message", message);

        // Simpan ke koleksi "notification" di Firestore
        firestore.collection("notification").add(notificationData)
                .addOnSuccessListener(documentReference -> {
                    // Document successfully written
                })
                .addOnFailureListener(e -> {
                    // Handle possible errors.
                });
    }
}
