package com.cs.tvshows.utils

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.downloadImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .into(this)
}

fun RecyclerView.onScrolledToEnd(onLoadMoreListener: (() -> Unit)?) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 && recyclerView.isLastItemDisplayed()) {
                onLoadMoreListener?.invoke()
            }
        }
    })
}

fun RecyclerView.isLastItemDisplayed(): Boolean {
    return hasItems().let { hasItems ->
        adapter?.let {
            val staggeredGridLayoutManager = (layoutManager as StaggeredGridLayoutManager)
            val lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(
                IntArray(staggeredGridLayoutManager.spanCount)
            )
            val lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            hasItems && ((lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == it.itemCount - 1))
        } ?: false
    }
}

fun RecyclerView.hasItems(): Boolean {
    return adapter?.let { it.itemCount > 0 } ?: false
}

private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
    var maxSize = 0
    for (i in lastVisibleItemPositions.indices) {
        if (i == 0) {
            maxSize = lastVisibleItemPositions[i]
        } else if (lastVisibleItemPositions[i] > maxSize) {
            maxSize = lastVisibleItemPositions[i]
        }
    }
    return maxSize
}
