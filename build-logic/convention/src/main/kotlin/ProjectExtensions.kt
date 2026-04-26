import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.configureKotlinAndroid(
    commonExtension: Any,
) {
    if (commonExtension is ApplicationExtension) {
        commonExtension.apply {
            compileSdk = 37
            defaultConfig {
                minSdk = 28
                targetSdk = 37
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }
        }
    } else if (commonExtension is LibraryExtension) {
        commonExtension.apply {
            compileSdk = 37
            defaultConfig { minSdk = 28 }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
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

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findBundle("androidx-compose").get())
    }
}
