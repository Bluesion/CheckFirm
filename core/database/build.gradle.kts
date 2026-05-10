plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm.core.database"

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

room {
    schemaDirectory("$projectDir/schemas")
    generateKotlin = true
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room)
    implementation(libs.room.ktx)

    implementation(libs.bundles.hilt)

    ksp(libs.bundles.hilt.compiler)
    ksp(libs.room.compiler)
}
