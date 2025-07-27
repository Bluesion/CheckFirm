import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bluesion.oneui"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    sourceSets {
        getByName("main") {
            java {
                srcDir("src/divider/java")
                srcDir("src/edittext/java")
                srcDir("src/spinner/java")
                srcDir("src/switch/java")
                srcDir("src/tab/java")
            }

            res {
                srcDir("src/checkbox/res")
				srcDir("src/divider/res")
                srcDir("src/edittext/res")
				srcDir("src/fab/res")
				srcDir("src/progress/res")
				srcDir("src/radiobutton/res")
				srcDir("src/spinner/res")
                srcDir("src/switch/res")
                srcDir("src/tab/res")
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
}