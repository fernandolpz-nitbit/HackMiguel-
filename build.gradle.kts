// En TuProyecto/build.gradle.kts
plugins {
    // Define los plugins para todo el proyecto
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false // <- Declara que el plugin existe

}