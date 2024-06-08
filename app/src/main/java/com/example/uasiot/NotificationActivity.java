package com.example.uasiot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uasiot.Adapter.notifAdapter;
import com.example.uasiot.Model.notifModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotif;
    private notifAdapter NotifAdapter;
    private ImageView back;
    private List<notifModel> notifList;
    private FirebaseFirestore firestore;
    private TextView read, clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerViewNotif = findViewById(R.id.recnotif);
        recyclerViewNotif.setLayoutManager(new LinearLayoutManager(this));

        notifList = new ArrayList<>();
        NotifAdapter = new notifAdapter(this, notifList);
        recyclerViewNotif.setAdapter(NotifAdapter);
        read = findViewById(R.id.readNotif);
        clear = findViewById(R.id.clearnotif);
        firestore = FirebaseFirestore.getInstance();
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        fetchNotifications();

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (notifModel notif : notifList) {
                    notif.setRead(true);
                }
                NotifAdapter.notifyDataSetChanged();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNotifications();
            }
        });
    }

    private void fetchNotifications() {
        firestore.collection("notification")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error
                            return;
                        }

                        notifList.clear();
                        if (value != null) {
                            for (DocumentSnapshot document : value.getDocuments()) {
                                notifModel notif = document.toObject(notifModel.class);
                                notifList.add(notif);
                                document.getReference().update("read", false);  // Mark as read
                            }
                            NotifAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void clearNotifications() {
        firestore.collection("notification").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = firestore.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                batch.commit().addOnCompleteListener(batchTask -> {
                    if (batchTask.isSuccessful()) {
                        Toast.makeText(this, "Notifications cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear notifications", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Failed to get notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
