plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.example.savecar"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    // Add your other dependencies here
}