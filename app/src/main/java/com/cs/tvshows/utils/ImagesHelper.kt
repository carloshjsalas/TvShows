package com.cs.tvshows.utils

import android.widget.ImageView
import com.google.android.material.imageview.ShapeableImageView
import javax.inject.Inject

class ImagesHelper @Inject constructor() {

    private val baseImageUrl = "https://image.tmdb.org/t/p/"
    private val imageTypeRegular = "w500"
    private val imageTypeOriginal = "original"

    fun retrieveImageForListItem(imageUrl: String?, imageView: ImageView) {
        if (imageUrl != null) {
            imageView.downloadImage(getUrlForRegularImage(imageUrl))
        }
    }

    fun retrieveOriginalImage(imageUrl: String?, imageView: ImageView) {
        if (imageUrl != null) {
            imageView.downloadImage(getUrlForOriginalImage(imageUrl))
        }
    }

    private fun getUrlForRegularImage(imageUrl: String) = baseImageUrl + imageTypeRegular + imageUrl

    private fun getUrlForOriginalImage(imageUrl: String) =
        baseImageUrl + imageTypeOriginal + imageUrl
}
