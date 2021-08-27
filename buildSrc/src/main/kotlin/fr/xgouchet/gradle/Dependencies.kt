package fr.xgouchet.gradle

@Suppress("unused")
object Dependencies {

    object Versions {

        const val AndroidPlugin = "4.0.0"

        const val AndroidXAnnotations = "1.1.0"
        const val AndroidXAppCompat = "1.2.0"
        const val AndroidXArch = "2.0.0"
        const val AndroidXCore = "1.3.2"
        const val AndroidXMedia = "1.2.0"
        const val AndroidXNav = "2.1.0-alpha02"
        const val AndroidXRecyclerView = "1.1.0"
        const val AndroidXTest = "1.1.0"
        const val AndroidMaven = "2.1"
        const val MultiDex = "2.0.1"
        const val ConstraintLayout = "2.0.2"
        const val MaterialComponents = "1.2.1"
        const val Gson = "2.8.6"

        const val BuildTimeTracker = "0.11.0"
        const val DependencyVersion = "0.33.0"
        const val Detekt = "1.14.1"
        const val Kotlin = "1.4.10"
        const val RxJava = "3.0.0"
        const val RxAndroid = "3.0.0"
        const val AboutPage = "1.3"

        const val Leakcanary = "1.5.4"
        const val Timber = "4.7.1"
        const val Stetho = "1.5.0"
        const val AXml = "v1.0.1"
    }

    object Libraries {

        @JvmField val AndroidX = arrayOf(
                "androidx.annotation:annotation:${Versions.AndroidXAnnotations}",
                "androidx.appcompat:appcompat:${Versions.AndroidXAppCompat}",
                "androidx.core:core-ktx:${Versions.AndroidXCore}",
                "androidx.media:media:${Versions.AndroidXMedia}"
        )

        @JvmField val AndroidUI = arrayOf(
                "androidx.constraintlayout:constraintlayout:${Versions.ConstraintLayout}",
                "androidx.recyclerview:recyclerview:${Versions.AndroidXRecyclerView}",
                "com.google.android.material:material:${Versions.MaterialComponents}"
        )

        const val MultiDex = "androidx.multidex:multidex:${Versions.MultiDex}"

        @JvmField
        val Kotlin = arrayOf(
                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin}",
                "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin}"
        )
        @JvmField
        val Rx = arrayOf(
                "io.reactivex.rxjava3:rxjava:${Versions.RxJava}",
                "io.reactivex.rxjava3:rxandroid:${Versions.RxAndroid}"
        )

        const val Gson = "com.google.code.gson:gson:${Versions.Gson}"
        const val AboutPage = "com.github.medyo:android-about-page:${Versions.AboutPage}"
        const val AXml = "com.github.xgouchet:AXML:${Versions.AXml}"

        const val Timber = "com.jakewharton.timber:timber:${Versions.Timber}"
    }

    object ClassPaths {
        const val AndroidPlugin = "com.android.tools.build:gradle:${Versions.AndroidPlugin}"
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
