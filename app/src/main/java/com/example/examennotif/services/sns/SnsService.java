package com.example.examennotif.services.sns;

import android.util.Log;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointRequest;
import software.amazon.awssdk.services.sns.model.CreatePlatformEndpointResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import com.example.examennotif.config.AwsConfig;

public class SnsService {

    private static final String TAG = "AWS_SNS";
    private static final String PLATFORM_ARN = "arn:aws:sns:eu-west-3:006968805961:app/GCM/NotifPush";
    private static final String TOPIC_ARN = "arn:aws:sns:eu-west-3:006968805961:NotifPushTopic";

    // Enregistre un appareil sur AWS SNS et retourne son Endpoint ARN
    public static String registerDevice(String fcmToken) {
        try (SnsClient snsClient = AwsConfig.getSnsClient()) {
            CreatePlatformEndpointResponse response = snsClient.createPlatformEndpoint(
                    CreatePlatformEndpointRequest.builder()
                            .platformApplicationArn(PLATFORM_ARN)
                            .token(fcmToken)
                            .build()
            );
            return response.endpointArn();
        } catch (Exception e) {
            Log.e(TAG, "Échec de l'enregistrement de l'appareil sur SNS", e);
            return null;
        }
    }

    // Abonne un appareil au topic SNS
    public static void subscribeToTopic(String endpointArn) {
        if (endpointArn == null) {
            Log.e(TAG, "Impossible d'abonner un endpoint NULL");
            return;
        }

        try (SnsClient snsClient = AwsConfig.getSnsClient()) {
            snsClient.subscribe(
                    SubscribeRequest.builder()
                            .topicArn(TOPIC_ARN)
                            .protocol("application")
                            .endpoint(endpointArn)
                            .build()
            );
            Log.d(TAG, "Appareil abonné au topic SNS !");
        } catch (Exception e) {
            Log.e(TAG, "Échec de l'abonnement au topic SNS", e);
        }
    }
}