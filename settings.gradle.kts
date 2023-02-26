pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info")
        maven ("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "Guise"

include(
    ":app",
    ":androidc",
    ":ktxposed"
)
