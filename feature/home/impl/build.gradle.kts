plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.home"
}

dependencies {
    implementation(projects.feature.home.api)
}
