@file:Suppress("UnstableApiUsage")

val version = ModuleVersion(rootProject.file("app/version.properties"))

plugins {
    id("kotlinx-serialization")
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}

android {
    namespace = "com.houvven.twig"
    compileSdk = ASdk.compile

    signingConfigs {
        val signatureInfo = SignatureInfo(rootProject.file("local.properties"))
        create("release") {
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            storeFile = signatureInfo.storeFileFromPath
            storePassword = signatureInfo.storePassword
            keyAlias = signatureInfo.keyAlias
            keyPassword = signatureInfo.keyPassword
        }
    }

    defaultConfig {
        applicationId = namespace
        minSdk = ASdk.min
        targetSdk = ASdk.target
        versionCode = version.versionCode
        versionName = version.versionName
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    compileOnly(project(":abcl"))
    implementation(project(":androidc"))
    implementation(project(":ktxposed"))
    compileOnly(libs.xposed.api)
    implementation(libs.mmkv)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    // Ktor
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    // Accompanist
    implementation(libs.accompanist.webview)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation (libs.accompanist.drawablepainter)
    // Lottie
    implementation(libs.lottie.compose)
    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    // Compose
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // https://mvnrepository.com/artifact/androidx.compose.material3/material3
    @Suppress("UseTomlInstead")
    implementation("androidx.compose.material3:material3:1.1.0-alpha08")
    implementation("androidx.compose.material:material-icons-extended")
    // Compose Rich Text
    implementation(libs.richtext.commonmark)
}

tasks.register("update-version") {
    doLast {
        version.updateVersion()
    }
}
