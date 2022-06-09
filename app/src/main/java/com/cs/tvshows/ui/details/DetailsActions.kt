package com.cs.tvshows.ui.details

import com.cs.tvshows.data.model.TvShow

sealed class DetailsActions {
    class OnTvShowLoaded(val item: TvShow) : DetailsActions()
}