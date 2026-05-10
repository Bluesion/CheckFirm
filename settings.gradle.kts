pluginManagement {
    includeBuild("build-logic")
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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "CheckFirm"
include(":app")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:navigation")
include(":core:network")
include(":core:preference:api")
include(":feature:bookmark:api")
include(":feature:bookmark:impl")
include(":feature:category:api")
include(":feature:category:impl")
include(":feature:forceupdate:api")
include(":feature:forceupdate:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:report:api")
include(":feature:report:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
include(":feature:sherlock:api")
include(":feature:sherlock:impl")
