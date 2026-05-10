plugins {
    id("checkfirm.android.feature")
}

android {
    namespace = "com.illusion.checkfirm.feature.category"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.feature.category.api)
}
