plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.bookmark"
}

dependencies {
    implementation(projects.feature.bookmark.api)
}
