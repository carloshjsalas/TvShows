package com.cs.tvshows.data.source.remote

import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.data.model.TvShowsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("tv/popular/")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): TvShowsListResponse

    @GET("tv/{tv_show_id}")
    suspend fun getTvShowDetails(
        @Path("tv_show_id") tvShowId: Int,
        @Query("api_key") apiKey: String
    ): TvShow
}
