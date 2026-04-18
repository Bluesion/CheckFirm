plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.illusion.checkfirm.core.preference.impl"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28

    }

    buildFeatures {
        compose = true
    }

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.preference.api)
    implementation(projects.domain)
    implementation(libs.datastore)

    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.hilt)

    ksp(libs.bundles.hilt.compiler)
}
