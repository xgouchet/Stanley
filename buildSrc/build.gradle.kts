plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    id("java-gradle-plugin")
}

buildscript {
    repositories {
        mavenCentral()
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlinStdlibJdk8)
    implementation(libs.androidBuildTools)
    implementation(libs.fuel)
    implementation(libs.fuelGson)
}
// region Kotlin

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

// endregion

// region Custom Plugins

gradlePlugin {
    plugins {
        register("ossLicenses") {
            id = "ossLicenses" // the alias
            implementationClass = "fr.xgouchet.gradle.plugin.OSSPlugin"
        }
    }
}

// endregion
