plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.settings"
}

dependencies {
    implementation(projects.feature.settings.api)
    implementation(libs.bundles.androidx.navigation3)
}
