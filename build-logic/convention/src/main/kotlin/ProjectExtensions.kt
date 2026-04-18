import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.configureKotlinAndroid(
    commonExtension: Any,
) {
    if (commonExtension is ApplicationExtension) {
        commonExtension.apply {
            compileSdk = 36
            defaultConfig { minSdk = 28 }
            compileOptions {
                sourceCompatibility = org.gradle.api.JavaVersion.VERSION_21
                targetCompatibility = org.gradle.api.JavaVersion.VERSION_21
            }
        }
    } else if (commonExtension is LibraryExtension) {
        commonExtension.apply {
            compileSdk = 36
            defaultConfig { minSdk = 28 }
            compileOptions {
                sourceCompatibility = org.gradle.api.JavaVersion.VERSION_21
                targetCompatibility = org.gradle.api.JavaVersion.VERSION_21
            }
        }
    }
}

internal fun Project.configureAndroidCompose(
    commonExtension: Any,
) {
    if (commonExtension is ApplicationExtension) {
        commonExtension.apply {
            buildFeatures { compose = true }
        }
    } else if (commonExtension is LibraryExtension) {
        commonExtension.apply {
            buildFeatures { compose = true }
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findBundle("androidx-compose").get())
    }
}
