plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("kotlin-kapt") // Χρειάζεται για το Hilt (προς το παρόν)
}

android {
    namespace = "com.example.pokemonexplorer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokemonexplorer"
        minSdk = 24
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
        // Αυτή η έκδοση είναι συμβατή με Kotlin 1.9.0
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Logging Interceptor (Το κρατάμε για να βλέπουμε τα logs του δικτύου)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    // implementation(libs.okhttp.logging) <-- Το έχουμε βάλει χειροκίνητα πιο πάνω, οπότε οκ

    // Images
    implementation(libs.coil.compose)

    // --- TESTING DEPENDENCIES (ΝΕΕΣ ΠΡΟΣΘΗΚΕΣ) ---

    // Βασικό Testing
    testImplementation(libs.junit)

    // MockK: Για να φτιάχνουμε ψεύτικα Repositories
    testImplementation("io.mockk:mockk:1.13.9")

    // Coroutines Test: Για να ελέγχουμε τον χρόνο στα tests (advanceUntilIdle)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Architecture Testing: Βοηθάει με τα LiveData/State
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Android InstrumentedTests (Αυτά τρέχουν σε συσκευή)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}