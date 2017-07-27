package fr.xgouchet.packageexplorer.core.utils

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.TextView


/**
 * @author Xavier F. Gouchet
 */
object Databindings {

    @BindingAdapter("android:drawableStart")
    @JvmStatic
    fun setImageViewResource(textView: TextView,
                             @DrawableRes resource: Int) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, 0, 0, 0)
    }
}