plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.report"
}

dependencies {
    implementation(projects.feature.report.api)
    implementation(libs.bundles.angus)
}
