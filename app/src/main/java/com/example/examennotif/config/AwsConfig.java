package com.example.examennotif.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import com.example.examennotif.BuildConfig;

public class AwsConfig {

    public static SnsClient getSnsClient() {
        return SnsClient.builder()
                .region(Region.of(BuildConfig.AWS_REGION))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        BuildConfig.AWS_ACCESS_KEY_ID,
                                        BuildConfig.AWS_SECRET_ACCESS_KEY
                                )
                        )
                )
                .build();
    }
}
