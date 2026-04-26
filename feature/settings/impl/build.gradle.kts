plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.settings"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(projects.core.preference.api)
    implementation(projects.domain)
    implementation(libs.bundles.androidx.navigation3)
    implementation(libs.bundles.firebase)
}
