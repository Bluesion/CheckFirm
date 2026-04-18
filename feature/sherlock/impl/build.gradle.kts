plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.sherlock"
}

dependencies {
    implementation(projects.feature.sherlock.api)
}
