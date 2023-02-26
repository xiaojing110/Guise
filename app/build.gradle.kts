@file:Suppress("UnstableApiUsage")

val version = ModuleVersion(rootProject.file("app/version.properties"))

plugins {
    id("kotlinx-serialization")
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.houvven.guise"
    compileSdk = ASdk.compile

    signingConfigs {
        val signatureInfo = SignatureInfo(rootProject.file("local.properties"))
        create("release") {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            runCatching { signatureInfo.storeFileFromPath }.onSuccess { storeFile = it }
            runCatching { signatureInfo.storePassword }.onSuccess { storePassword = it }
            runCatching { signatureInfo.keyAlias }.onSuccess { keyAlias = it }
            runCatching { signatureInfo.keyPassword }.onSuccess { keyPassword = it }
        }
    }

    defaultConfig {
        applicationId = namespace
        minSdk = ASdk.min
        targetSdk = ASdk.target
        versionCode = version.versionCode
        versionName = version.versionName
        signingConfig = signingConfigs.getByName("release")
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        dex {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(project(":androidc"))
    implementation(project(":ktxposed"))
    compileOnly(libs.xposed.api)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.accompanist.webview)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation (libs.lottie.compose)
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
}

tasks.register("update-version") {
    doLast {
        version.updateVersion()
    }
}
