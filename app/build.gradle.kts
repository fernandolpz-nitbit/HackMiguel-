// Archivo: app/build.gradle.kts (VERSIÓN FINAL Y COMPLETA)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Aplica el plugin de Google Services a este módulo
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.tuempresa.saludtotal.test"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.tuempresa.saludtotal.test"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Dependencias existentes de AndroidX y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Dependencias para testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- DEPENDENCIAS DE FIREBASE AÑADIDAS ---
    // Importa el Bill of Materials (BoM) de Firebase para gestionar versiones
    implementation(platform(libs.firebase.bom))
    // Dependencia para Firebase Authentication
    implementation(libs.firebase.auth)
    // Dependencia para la base de datos Cloud Firestore
    implementation(libs.firebase.firestore)
    // ------------------------------------------
    implementation("com.google.android.gms:play-services-auth:21.2.0")
}