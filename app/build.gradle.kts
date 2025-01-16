

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.ridenow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ridenow"
        minSdk = 24
        targetSdk = 34
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
    implementation("com.google.firebase:firebase-bom:32.0.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.firebase:firebase-auth:21.6.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.maps:google-maps-services:0.15.0")




    implementation ("com.stripe:stripe-android:20.48.0")
        implementation ("com.squareup.okhttp3:okhttp:4.11.0")// For network requests




    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}