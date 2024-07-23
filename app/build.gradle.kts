plugins {
    id("com.android.application")
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dependencyVersions)
    id("ossLicenses")
}

android {
    compileSdk = fr.xgouchet.gradle.AndroidBuild.TargetSdk
    buildToolsVersion = fr.xgouchet.gradle.AndroidBuild.BuildTools

    defaultConfig {
        minSdk = fr.xgouchet.gradle.AndroidBuild.MinSdk
        targetSdk = fr.xgouchet.gradle.AndroidBuild.TargetSdk
        versionCode = 20
        versionName = "2.8"
        multiDexEnabled = true

        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
	    release {
		    storeFile System.getenv("STORE_FILE")
		    storePassword System.getenv("STORE_PASSWORD")
		    keyAlias System.getenv("KEY_ALIAS")
		    keyPassword System.getenv("KEY_PASSWORD")
	    }
    }

    namespace = "fr.xgouchet.packageexplorer"

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.androidUi)
    implementation(libs.multidex)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.rx)
    implementation(libs.gson)
    implementation(libs.aboutPage)
    implementation(libs.timber)
}

// region Kotlin

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

// endregion

// region Detekt

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "11"
    config.setFrom("${project.rootDir}/config/detekt/detekt.yml")
}

// endregion

// region DependencyUpdates

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>().configureEach {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

// endregion
