plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.forceupdate"
}

dependencies {
    implementation(projects.feature.forceupdate.api)
}
