package fr.xgouchet.packageexplorer.core.utils

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.ImageView

@BindingAdapter("app:srcVector")
fun setSrcVector(view: ImageView, @DrawableRes drawable: Int) {
    view.setImageResource(drawable)
}

