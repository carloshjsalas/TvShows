package com.cs.tvshows.data.model

import com.squareup.moshi.Json

data class TvShowsListResponse(
    @Json(name = "page") val page: Int,
    @Json(name = "results") val tvShows: List<TvShow>,
    @Json(name = "total_pages") val totalPages: Int
)
