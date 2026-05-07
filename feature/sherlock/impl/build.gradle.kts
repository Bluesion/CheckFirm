plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.sherlock"
}

dependencies {
    implementation(projects.feature.sherlock.api)
    implementation(projects.core.preference.api)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.androidx.navigation3)
}
