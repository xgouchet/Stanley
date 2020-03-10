package fr.xgouchet.gradle

@Suppress("unused")
object Dependencies {

    object Versions {

        const val AndroidPlugin = "3.4.0"

        const val AndroidSupportLibs = "28.0.0"
        const val AndroidX = "1.0.0"
        const val AndroidXArch = "2.0.0"
        const val AndroidXNav = "2.1.0-alpha02"
        const val AndroidXTest = "1.1.0"
        const val AndroidMaven = "2.1"
        const val MultiDex = "2.0.1"
        const val ConstraintLayout = "2.0.0-beta2"
        const val MaterialComponents = "1.1.0-alpha08"

        const val OssLicensesPlugin = "0.9.2"
        const val OssLicensesLibrary = "17.0.0"

        const val BuildTimeTracker = "0.11.0"
        const val DependencyVersion = "0.20.0"
        const val Detekt = "1.0.0.RC6-3"
        const val Kotlin = "1.3.41"
        const val RxJava = "2.2.6"
        const val RxAndroid = "2.1.1"
        const val AboutPage = "1.2.5"

        const val Leakcanary = "1.5.4"
        const val Timber = "4.7.1"
        const val Stetho = "1.5.0"
        const val AXml = "v1.0.1"
    }

    object Libraries {

        const val AndroidAppCompat = "androidx.appcompat:appcompat:${Versions.AndroidX}"

        @JvmField val AndroidX = arrayOf(
                "androidx.annotation:annotation:${Versions.AndroidX}",
                "androidx.appcompat:appcompat:${Versions.AndroidX}",
                "androidx.core:core-ktx:${Versions.AndroidX}",
                "androidx.media:media:${Versions.AndroidX}"
        )

        @JvmField val AndroidUI = arrayOf(
                "androidx.constraintlayout:constraintlayout:${Versions.ConstraintLayout}",
                "androidx.recyclerview:recyclerview:${Versions.AndroidX}",
                "com.google.android.material:material:${Versions.MaterialComponents}"
        )

        @JvmField val AndroidSupport = arrayOf(
                "com.android.support:support-v4:${Versions.AndroidSupportLibs}",
                "com.android.support:appcompat-v7:${Versions.AndroidSupportLibs}",
                "com.android.support:support-annotations:${Versions.AndroidSupportLibs}",
                "com.android.support:design:${Versions.AndroidSupportLibs}",
                "com.android.support:cardview-v7:${Versions.AndroidSupportLibs}",
                "com.android.support:recyclerview-v7:${Versions.AndroidSupportLibs}",
                "com.android.support.constraint:constraint-layout:${Versions.ConstraintLayout}"
        )

        const val MultiDex = "androidx.multidex:multidex:${Versions.MultiDex}"

        @JvmField
        val Kotlin = arrayOf(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin}",
                "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"
        )
        @JvmField
        val Rx = arrayOf(
                "io.reactivex.rxjava2:rxjava:${Versions.RxJava}",
                "io.reactivex.rxjava2:rxandroid:${Versions.RxAndroid}"
        )

        const val OssLicences = "com.google.android.gms:play-services-oss-licenses:${Versions.OssLicensesLibrary}"
        const val AboutPage = "com.github.medyo:android-about-page:${Versions.AboutPage}"
        const val AXml = "com.github.xgouchet:AXML:${Versions.AXml}"

        const val Timber = "com.jakewharton.timber:timber:${Versions.Timber}"
    }

    object ClassPaths {
        const val AndroidPlugin = "com.android.tools.build:gradle:${Versions.AndroidPlugin}"
        const val OssLicencesPlugin = "com.google.gms:oss-licenses:${Versions.OssLicensesPlugin}"
        const val KotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
    }

    object PluginNamespaces {
        const val Detetk = "io.gitlab.arturbosch"
        const val DependencyVersion = "com.github.ben-manes"
        const val Kotlin = "org.jetbrains.kotlin"
    }

    object Repositories {
        const val Jitpack = "https://jitpack.io"
        const val Gradle = "https://plugins.gradle.org/m2/"
        const val Google = "https://maven.google.com"
    }

    object Processors
}
