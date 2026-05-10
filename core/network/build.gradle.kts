plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm.core.network"

    compileSdk = 37

    defaultConfig {
        minSdk = 28
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

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor)

    implementation(libs.firestore)
    implementation(libs.ksoup)

    implementation(libs.bundles.hilt)
    ksp(libs.bundles.hilt.compiler)
}
