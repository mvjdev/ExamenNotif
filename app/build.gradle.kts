import java.util.Properties
import java.io.File

val dotenv = Properties().apply {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.examennotif"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.examennotif"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "AWS_ACCESS_KEY_ID", "\"${dotenv["AWS_ACCESS_KEY_ID"]}\"")
            buildConfigField("String", "AWS_SECRET_ACCESS_KEY", "\"${dotenv["AWS_SECRET_ACCESS_KEY"]}\"")
            buildConfigField("String", "AWS_REGION", "\"${dotenv["AWS_REGION"]}\"")
        }
        debug {
            buildConfigField("String", "AWS_ACCESS_KEY_ID", "\"${dotenv["AWS_ACCESS_KEY_ID"]}\"")
            buildConfigField("String", "AWS_SECRET_ACCESS_KEY", "\"${dotenv["AWS_SECRET_ACCESS_KEY"]}\"")
            buildConfigField("String", "AWS_REGION", "\"${dotenv["AWS_REGION"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.play.services.tasks)
    implementation(libs.firebase.messaging)
    implementation(platform(libs.bom))
    implementation(libs.software.sns)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}