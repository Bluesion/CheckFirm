plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.catcher"
}

dependencies {
    implementation(projects.feature.catcher.api)
    implementation(projects.core.preference.api)
    implementation(libs.bundles.firebase)
}
