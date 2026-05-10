import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    id("checkfirm.android.application")
    id("checkfirm.android.application.compose")
    id("checkfirm.hilt")
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.gms)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm"

    defaultConfig {
        applicationId = "com.illusion.checkfirm"
        versionCode = 58
        versionName = "11.2.1"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            // signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += "META-INF/LICENSE.md"
            pickFirsts += "META-INF/NOTICE.md"
            pickFirsts += "META-INF/DEPENDENCIES"
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
    generateKotlin = true
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.domain)
    implementation(projects.core.network)

    // Bookmark
    implementation(projects.feature.bookmark.api)
    implementation(projects.feature.bookmark.impl)

    // Force update
    implementation(projects.feature.forceupdate.api)
    implementation(projects.feature.forceupdate.impl)

    // Main
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)

    // Report
    implementation(projects.feature.report.api)
    implementation(projects.feature.report.impl)

    // Search
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)

    // Settings
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)

    // Sherlock
    implementation(projects.feature.sherlock.api)
    implementation(projects.feature.sherlock.impl)

    // Core
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.preference.api)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.data)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.ktor)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ksoup)

    ksp(libs.bundles.hilt.compiler)
    ksp(libs.room.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)
}
