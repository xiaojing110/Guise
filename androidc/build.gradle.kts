plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.houvven.androidc"
    compileSdk = ASdk.compile

    defaultConfig {
        minSdk = ASdk.min
        consumerProguardFiles("consumer-rules.pro")
    }

    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
}