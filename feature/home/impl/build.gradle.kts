plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.home"
}

dependencies {
    implementation(projects.core.preference.api)
    implementation(projects.feature.bookmark.api)
    implementation(projects.feature.home.api)
    implementation(projects.feature.report.api)
    implementation(projects.feature.search.api)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.sherlock.api)
}
