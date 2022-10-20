package fr.xgouchet.packageexplorer.details

import fr.xgouchet.packageexplorer.R

data class NativeLibrary(
    private val fileName: String,
    private val cpuArchs: MutableSet<String>,
    val group: Int
) {

    fun withArch(arch: String) {
        cpuArchs.add(arch)
    }

    fun description(): String {
        return "$fileName (${cpuArchs.joinToString(", ")})"
    }

    companion object {

        private val KNOWN_NATIVE_LIBS = mapOf(
            "libRSSupport.so" to R.string.native_lib_group_renderscript,
            "libaesgcm.so" to R.string.native_lib_group_aesgcm,
            "libarcore_sdk_jni.so" to R.string.native_lib_group_arcore,
            "libc++_shared.so" to R.string.native_lib_group_cppshared,
            "libconscrypt_jni.so" to R.string.native_lib_group_conscrypt,
            "libcrashlytics-common.so" to R.string.native_lib_group_crashlytics,
            "libcrashlytics-handler.so" to R.string.native_lib_group_crashlytics,
            "libcrashlytics-trampoline.so" to R.string.native_lib_group_crashlytics,
            "libcrashlytics.so" to R.string.native_lib_group_crashlytics,
            "libhermes-executor-common-debug.so" to R.string.native_lib_group_hermes,
            "libhermes-executor-common-release.so" to R.string.native_lib_group_hermes,
            "libhermes-executor-debug.so" to R.string.native_lib_group_hermes,
            "libhermes-executor-release.so" to R.string.native_lib_group_hermes,
            "libhermes-inspector.so" to R.string.native_lib_group_hermes,
            "libmobilecoin.so" to R.string.native_lib_group_mobilecoin,
            "libringrtc.so" to R.string.native_lib_group_ringrtc,
            "libringrtc_rffi.so" to R.string.native_lib_group_ringrtc,
            "librsjni-androidx.so" to R.string.native_lib_group_renderscript,
            "librsjni.so" to R.string.native_lib_group_renderscript,
            "libsentry-android.so" to R.string.native_lib_group_sentry,
            "libsentry.so" to R.string.native_lib_group_sentry,
            "libsqlcipher.so" to R.string.native_lib_group_sqlcipher,
            "libsqlite.so" to R.string.native_lib_group_sqlite,
        )

        fun from(fileName: String): NativeLibrary {
            val group = KNOWN_NATIVE_LIBS[fileName] ?: R.string.native_lib_group_unknown
            return NativeLibrary(fileName, mutableSetOf(), group)
        }

        fun from(fileName: String, arch: String): NativeLibrary {
            val group = KNOWN_NATIVE_LIBS[fileName] ?: R.string.native_lib_group_unknown
            return NativeLibrary(fileName, mutableSetOf(arch), group)
        }
    }
}
