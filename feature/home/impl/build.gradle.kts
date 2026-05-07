plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.home"
}

dependencies {
    implementation(projects.feature.home.api)
    implementation(projects.core.preference.api)
    implementation(projects.feature.sherlock.api)
    implementation(projects.feature.report.api)
    implementation(projects.feature.settings.api)
}
