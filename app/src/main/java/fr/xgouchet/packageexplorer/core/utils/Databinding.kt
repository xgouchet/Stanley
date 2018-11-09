package fr.xgouchet.packageexplorer.core.utils

import androidx.databinding.BindingAdapter
import androidx.annotation.DrawableRes
import android.widget.ImageView

@BindingAdapter("app:srcVector")
fun setSrcVector(view: ImageView, @DrawableRes drawable: Int) {
    view.setImageResource(drawable)
}

