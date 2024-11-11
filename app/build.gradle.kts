plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.classmate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.classmate"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.paging:paging-compose:3.3.0-alpha05")
    implementation ("androidx.compose.material3:material3:<latest_version>")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation(libs.firebase.storage)
    implementation("androidx.compose.material:material:1.7.1")
    // Todo el módulo de navegación incluye los composables NavHost y NavController. También permite instanciar de forma simple ViewModel()
    implementation("androidx.navigation:navigation-compose:2.7.7")
// Incluye los objetos LiveData y MutableLiveData. Además del método observeAsState()
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
// Nos permite cargar imágenes de la web en un composable de imagen de forma asincrónica
    implementation(libs.coil.compose)
// Nos permite serializar objetos en JSON
    implementation("com.google.code.gson:gson:2.11.0")
//Nos permite usar las corutinas en el contexto de ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
//Nos permite usar await para llamados a la red
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

    implementation ("androidx.compose.material:material:1.4.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.interop)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.paging.common.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}