package fr.xgouchet.packageexplorer.core.utils

import android.content.Context
import android.content.SharedPreferences
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * This provide field delegates to ease the link between a view and a class
 *
 * @author Xavier F. Gouchet
 */
object Notebook {

    fun <P : ContextHolder> notebook(key: String, default: Boolean): ReadWriteProperty<P, Boolean> = BooleanPage(key, default)

    fun <P : ContextHolder> notebook(key: String, default: Int): ReadWriteProperty<P, Int> = IntPage(key, default)

    fun <P : ContextHolder> notebook(key: String, default: String): ReadWriteProperty<P, String> = StringPage(key, default)

    fun <P : ContextHolder> notebook(key: String, default: AppSort): ReadWriteProperty<P, AppSort> = AppSortPage(key, default)

}

/**
 * An object which can provide a context
 */
interface ContextHolder {
    val context: Context
}

/**
 * A generic Read Write property reading and saving to/from the default shared preferences
 */
abstract class GenericPage<in P : ContextHolder, T : Any> : ReadWriteProperty<P, T> {
    var memoized: WeakReference<T> = WeakReference<T>(null)

    override fun getValue(thisRef: P, property: KProperty<*>): T {
        return memoized.get() ?: getPreferenceValue(getSharedPreferences(thisRef.context))
    }

    override fun setValue(thisRef: P, property: KProperty<*>, value: T) {
        memoized = WeakReference(value)

        val editor = getSharedPreferences(thisRef.context)
                .edit()
        saveValue(editor, value)
        editor.apply()
    }

    abstract fun saveValue(editor: SharedPreferences.Editor, value: T)

    abstract fun getPreferenceValue(preferences: SharedPreferences): T

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
    }
}

/**
 * A Boolean delegate reading from / writing to the default shared preferences
 */
class BooleanPage<in P : ContextHolder>(val key: String, val default: Boolean) : GenericPage<P, Boolean>() {
    override fun getPreferenceValue(preferences: SharedPreferences): Boolean {
        return preferences.getBoolean(key, default)
    }

    override fun saveValue(editor: SharedPreferences.Editor, value: Boolean) {
        editor.putBoolean(key, value)
    }
}

/**
 * A Int delegate reading from / writing to the default shared preferences
 */
class IntPage<in P : ContextHolder>(val key: String, val default: Int) : GenericPage<P, Int>() {
    override fun getPreferenceValue(preferences: SharedPreferences): Int {
        return preferences.getInt(key, default)
    }

    override fun saveValue(editor: SharedPreferences.Editor, value: Int) {
        editor.putInt(key, value)
    }
}

/**
 * A String delegate reading from / writing to the default shared preferences
 */
class StringPage<in P : ContextHolder>(val key: String, val default: String) : GenericPage<P, String>() {
    override fun getPreferenceValue(preferences: SharedPreferences): String {
        return preferences.getString(key, default)
    }

    override fun saveValue(editor: SharedPreferences.Editor, value: String) {
        editor.putString(key, value)
    }
}

/**
 * An Enum delegate reading from / writing to the default shared preferences
 */
class AppSortPage<in P : ContextHolder>(val key: String, val default: AppSort) : GenericPage<P, AppSort>() {

    override fun saveValue(editor: SharedPreferences.Editor, value: AppSort) {
        editor.putString(key, value.name)
    }

    override fun getPreferenceValue(preferences: SharedPreferences): AppSort {
        val name = preferences.getString(key, default.name)

        try {
            return AppSort.valueOf(name)
        } catch (e: IllegalArgumentException) {
            return default
        }
    }

}