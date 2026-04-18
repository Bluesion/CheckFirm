import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("checkfirm.android.library")
                apply("checkfirm.android.library.compose")
                apply("checkfirm.hilt")
            }

            dependencies {
                add("implementation", project(":domain"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:navigation"))
            }
        }
    }
}
