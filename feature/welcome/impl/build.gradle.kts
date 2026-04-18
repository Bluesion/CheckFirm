plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.welcome"
}

dependencies {
    implementation(projects.feature.welcome.api)
}
