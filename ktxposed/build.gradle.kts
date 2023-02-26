plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.houvven.ktxposed"
    compileSdk = ASdk.compile

    defaultConfig {
        minSdk = ASdk.min
        consumerProguardFiles("consumer-rules.pro")
    }

    @Suppress("UnstableApiUsage")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}

dependencies {
    compileOnly(libs.xposed.api)
    implementation(project(":androidc"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
}