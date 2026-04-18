plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.search"
}

dependencies {
    implementation(projects.feature.search.api)
}
