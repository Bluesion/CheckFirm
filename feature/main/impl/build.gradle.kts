plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.main"
}

dependencies {
    implementation(projects.feature.main.api)
}
