package fr.xgouchet.packageexplorer.core.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Xavier F. Gouchet
 */
object Cutelry {

    fun <V : View> knife(id: Int): ReadOnlyProperty<Any, V> = BreadKnife(id)
    fun <V : View> knife(id: Int, parent: View): ReadOnlyProperty<Any, V> = OysterKnife(id, parent)

}

@Suppress("UNCHECKED_CAST")
class BreadKnife<out V : View>(val id: Int) : ReadOnlyProperty<Any, V> {

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        if (thisRef is Activity) {
            return thisRef.findViewById(id) as V
        } else if (thisRef is Fragment) {
            return thisRef.view?.findViewById(id) as V
        } else if (thisRef is RecyclerView.ViewHolder) {
            return thisRef.itemView?.findViewById(id) as V
        } else {
            throw IllegalArgumentException("Can't find view by id in instance $thisRef")
        }
    }
}

@Suppress("UNCHECKED_CAST")
class OysterKnife<out V : View>(val id: Int, val parent: View) : ReadOnlyProperty<Any, V> {

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        return parent.findViewById(id) as V
    }
}