plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Garante que está usando a versão correta do Kotlin (ex: 2.0.0)
    alias(libs.plugins.kotlin.compose) // Garante que está usando a versão correta do Kotlin
    id("com.google.devtools.ksp")      // Aplica o plugin KSP
    id("com.google.dagger.hilt.android") // Aplica o plugin Hilt
}

android {
    namespace = "com.lcsz.first"
    compileSdk = 35 // Sugestão: Usar 34 para maior estabilidade atual (Android 14)
    // a menos que você precise especificamente de APIs do Android 15 (API 35)

    defaultConfig {
        applicationId = "com.lcsz.first"
        minSdk = 24
        targetSdk = 35 // Sugestão: Usar 34
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigação das telas
    implementation(libs.androidx.navigation.compose)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Retrofit
    implementation(libs.retrofit)
    // Conversor JSON (moshi)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    // Interceptor (debug http requests)
    implementation(libs.logging.interceptor)

    // --- Hilt Dependências Corrigidas ---
    implementation(libs.hilt.android) // Usará a versão definida em [versions] hilt (ex: "2.51.1")
    ksp(libs.hilt.compiler)          // Usará a versão definida em [versions] hilt (ex: "2.51.1")

    // Para integração do Hilt com Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)
}