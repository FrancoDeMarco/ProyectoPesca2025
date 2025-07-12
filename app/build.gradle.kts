
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "unpsjb.tnt.appdepesca"
    compileSdk = 34

    defaultConfig {
        applicationId = "unpsjb.tnt.appdepesca"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.protolite.well.known.types)
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.7")

    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.camera.core)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.compose.material3:material3:1.2.0")


    implementation("io.coil-kt:coil-compose:2.4.0")

    //para el filtrado por fechas
    implementation("com.google.android.material:material:1.11.0")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //dependencias de google maps
    implementation("com.google.maps.android:maps-compose:4.3.3") // versión actual a julio 2025
    implementation("com.google.android.gms:play-services-maps:18.2.0")
}

