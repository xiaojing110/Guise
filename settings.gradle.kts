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
        // Xposed API 只发布在 api.xposed.info，必须优先于国内镜像解析；
        // 镜像只返回 POM、缺少 api-82.jar 时会导致构建直接失败。
        maven("https://api.xposed.info") {
            content {
                includeGroup("de.robv.android.xposed")
            }
        }
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/") {
            content {
                excludeGroup("de.robv.android.xposed")
            }
        }
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "Guise"

include(
    ":app",
    ":androidc",
    ":ktxposed"
)
include(":abcl")
