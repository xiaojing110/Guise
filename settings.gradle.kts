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
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven("https://api.xposed.info")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "Twig"

include(
    ":app",
    ":androidc",
    ":ktxposed"
)
include(":abcl")
