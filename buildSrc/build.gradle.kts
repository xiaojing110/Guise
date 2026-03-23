import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

// buildSrc 必须自行声明仓库（不继承根项目的 repositories）
repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

// 版本号与根项目 gradle/libs.versions.toml 保持一致
val gradleVersion = "7.4.2"
val kotlinVersion = "1.8.10"

dependencies {
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}
