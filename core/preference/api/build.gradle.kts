plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
