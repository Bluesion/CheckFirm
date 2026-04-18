plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.catcher"
}

dependencies {
    implementation(projects.feature.catcher.api)
    implementation(libs.bundles.firebase)
}
