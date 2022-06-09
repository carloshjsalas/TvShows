package com.cs.tvshows.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("poster")
fun ImageView.poster(
    url: String?
) {
    url?.let { ImagesHelper().retrieveImageForListItem(url, this) }
}

@BindingAdapter("cover")
fun ImageView.cover(
    coverUrl: String?,
) {
    coverUrl?.let {
        ImagesHelper().retrieveOriginalImage(coverUrl, this)
    }
}

@BindingAdapter("text")
fun TextView.text(
    textString: String?
) {
    if (!textString.isNullOrEmpty()) {
        isVisible = true
        text = textString
    } else {
        isVisible = false
    }
}

@BindingAdapter("rating")
fun TextView.rating(
    rating: Double?
) {
    rating?.let {
        text = it.toString()
    }
}
