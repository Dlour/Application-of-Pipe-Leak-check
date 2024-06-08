package com.example.uasiot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uasiot.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseFirestore firestore;
    LinearLayout tagihanbtn, inforbtn, allbtn;

    ImageView allIcon, tagihanIcon, informasiIcon, notif;
    TextView allText, tagihanText, informasiText, userNameTextView;
    private View notifIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize buttons
        allbtn = findViewById(R.id.allbtn);
        inforbtn = findViewById(R.id.informasibtn);
        tagihanbtn = findViewById(R.id.tagihanbtn);

        // Initialize icons and texts
        allIcon = findViewById(R.id.allicon);
        tagihanIcon = findViewById(R.id.tagihanicon);
        informasiIcon = findViewById(R.id.informasiicon);
        allText = findViewById(R.id.allText);
        tagihanText = findViewById(R.id.tagihanText);
        informasiText = findViewById(R.id.informasiText);
        userNameTextView = findViewById(R.id.user_name);
        notif = findViewById(R.id.notif);
        notifIndicator = findViewById(R.id.notifIndicator);

        // Display user email
        displayUserEmail();

        // Check unread notifications
        checkUnreadNotifications();

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "terpencet", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                finish();
            }
        });

        loadFragment(new AllFragment());
        setActiveButton(allbtn);

        allbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AllFragment());
                setActiveButton(allbtn);
            }
        });

        inforbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new InformasiFragment());
                setActiveButton(inforbtn);
            }
        });

        tagihanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TagihanFragment());
                setActiveButton(tagihanbtn);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home, fragment);
        fragmentTransaction.commit();
    }

    private void setActiveButton(LinearLayout activeButton) {
        // Reset all buttons to default state
        resetButton(allbtn, R.drawable.circle_before, R.color.defaultTextColor);
        resetButton(inforbtn, R.drawable.circle_before, R.color.defaultTextColor);
        resetButton(tagihanbtn, R.drawable.circle_before, R.color.defaultTextColor);

        // Set active button
        if (activeButton == allbtn) {
            setButton(activeButton, R.drawable.circle_after, R.color.activeTextColor);
        } else if (activeButton == inforbtn) {
            setButton(activeButton, R.drawable.circle_after, R.color.activeTextColor);
        } else if (activeButton == tagihanbtn) {
            setButton(activeButton, R.drawable.circle_after, R.color.activeTextColor);
        }
    }

    private void resetButton(LinearLayout button, int iconRes, int textColorRes) {
        ImageView icon = (ImageView) button.getChildAt(0);
        TextView text = (TextView) button.getChildAt(1);

        icon.setImageResource(iconRes);
        text.setTextColor(ContextCompat.getColor(this, textColorRes));
    }

    public void setNotif() {
        String channelId = "default_channel_id";
        String channelName = "Default Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.tetesan)
                .setContentTitle("Test Notif ji")
                .setContentText("Notifkasik tekom e komekome")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, mbuilder.build());
    }

    private void setButton(LinearLayout button, int iconRes, int textColorRes) {
        ImageView icon = (ImageView) button.getChildAt(0);
        TextView text = (TextView) button.getChildAt(1);

        icon.setImageResource(iconRes);
        text.setTextColor(ContextCompat.getColor(this, textColorRes));
    }

    private void displayUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            userNameTextView.setText(email);
        } else {
            userNameTextView.setText("User not logged in");
        }
    }

    private void checkUnreadNotifications() {
        firestore.collection("notification")
                .whereEqualTo("read", false)  // assuming you have a "read" field in your notifications
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            notifIndicator.setVisibility(View.VISIBLE);
                        } else {
                            notifIndicator.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
