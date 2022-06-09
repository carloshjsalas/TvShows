package com.cs.tvshows.ui.home

import com.cs.tvshows.data.model.TvShow

sealed class HomeActions {
    class OnTvShowsFirstPageLoaded(val items: List<TvShow>) : HomeActions()
    class OnTvShowsNextPageLoaded(val items: List<TvShow>) : HomeActions()
}