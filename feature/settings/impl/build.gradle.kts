plugins {
    id("checkfirm.android.feature")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.illusion.checkfirm.feature.settings"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(projects.core.preference.api)
    implementation(projects.domain)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.firebase)
    implementation(libs.kotlinx.serialization.json)
}
