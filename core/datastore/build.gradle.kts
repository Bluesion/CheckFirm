plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.illusion.checkfirm.core.datastore"

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

dependencies {
    implementation(projects.core.preference.api)
    implementation(libs.datastore)

    implementation(libs.bundles.hilt)
    ksp(libs.bundles.hilt.compiler)
}
