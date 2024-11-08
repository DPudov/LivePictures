@file:Suppress("UnstableApiUsage")

include(":app:feature-gif")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LivePictures"
include(
    ":app",
    ":app:datasource-frames",
    ":app:domain",
    ":app:data"
)


buildCache {
    local {
        directory = File(rootDir, "build-cache")
    }
}