plugins {
    id("checkfirm.android.feature")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm.feature.settings"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(projects.feature.bookmark.api)
    implementation(projects.core.domain)
    implementation(projects.core.preference.api)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.firebase)
    implementation(libs.kotlinx.serialization.json)
}

dependencies { testImplementation("junit:junit:4.13.2") }
