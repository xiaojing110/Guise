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
        // 默认 debug 签名
        getByName("debug") { }

        // 尝试加载正式签名（支持 local.properties 文件 / 系统环境变量 / Base64）
        // 先检查环境变量或 local.properties 中是否存在签名配置
        val hasSigningEnv = System.getenv("SIGNATURE_STORE_FILE_BASE64") != null
                || System.getenv("SIGNATURE_STORE_FILE_PATH") != null
        val propsFile = rootProject.file("local.properties")
        val hasSigningProps = propsFile.exists() && propsFile.readText().contains("signature.store")
        val shouldLoadSigning = hasSigningEnv || hasSigningProps

        if (shouldLoadSigning) {
            try {
                val si = SignatureInfo(propsFile)
                // 优先从文件路径加载，失败则从 Base64 加载
                val storeFile = try { si.storeFileFromPath } catch (_: Exception) { si.storeFileFromBase64 }
                val storePassword = si.storePassword
                val keyAlias = si.keyAlias
                val keyPassword = si.keyPassword
                // 所有属性都成功获取后才创建 release 签名配置
                create("release") {
                    enableV1Signing = true
                    enableV2Signing = true
                    enableV3Signing = true
                    this.storeFile = storeFile
                    this.storePassword = storePassword
                    this.keyAlias = keyAlias
                    this.keyPassword = keyPassword
                }
                logger.lifecycle("✅ 正式签名配置已加载")
            } catch (e: Exception) {
                logger.lifecycle("⚠️ 签名配置加载失败，将使用 debug 签名。(${e.message})")
            }
        } else {
            logger.lifecycle("ℹ️ 未检测到签名配置，使用 debug 签名。")
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
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
        debug {
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
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
