package com.cs.tvshows.data.source.repository

import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.data.model.TvShowsListResponse
import com.cs.tvshows.utils.Outcome
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TvShowsRepository {
    suspend fun getTvShowsByPage(page: Int): Flow<Outcome<List<TvShow>>>
    suspend fun getTvShowDetails(tvShowId: Int): Flow<Outcome<TvShow>>
}