plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.bookmark"
}

dependencies {
    implementation(projects.core.preference.api)
    implementation(projects.feature.bookmark.api)
    implementation(projects.feature.category.api)
}
