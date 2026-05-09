plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.illusion.checkfirm.core.designsystem"

    compileSdk = 37

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
    compilerOptions.optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.androidx.compose)
    implementation(libs.bundles.haze)
    debugApi(libs.androidx.compose.ui.tooling)
}
