package com.example.examennotif;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.examennotif.config.AwsConfig;
import com.example.examennotif.services.sns.SnsService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AWS_SNS";

    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission accordée → Récupérer le token
                    getDeviceToken();
                } else {
                    Log.w(TAG, "Permission de notification refusée");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
    }

    // Demande la permission d'envoyer des notifications (Android 13+)
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                getDeviceToken();
            } else {
                resultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            getDeviceToken();
        }
    }

    // Récupère le token FCM et l'enregistre sur AWS SNS
    public void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Échec de récupération du token Firebase", task.getException());
                    return;
                }

                // Récupération du token Firebase
                String fcmToken = task.getResult();
                Log.d(TAG, "Token Firebase : " + fcmToken);

                // Enregistrer cet appareil sur AWS SNS
                String endpointArn = SnsService.registerDevice(fcmToken);
                Log.d(TAG, "Endpoint ARN SNS : " + endpointArn);

                // Abonner l'appareil au topic AWS SNS
                SnsService.subscribeToTopic(endpointArn);
            }
        });
    }
}
