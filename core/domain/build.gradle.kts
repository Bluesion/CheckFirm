plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(jdkVersion = 21)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
dependencies { testImplementation("junit:junit:4.13.2") }
