package com.cs.tvshows.data.source.repository

import androidx.room.withTransaction
import com.cs.tvshows.data.networkBoundResource
import com.cs.tvshows.data.source.local.TvShowsDatabase
import com.cs.tvshows.data.source.remote.ApiService
import com.cs.tvshows.utils.Constant
import com.cs.tvshows.utils.NetworkHelper
import kotlinx.coroutines.delay
import javax.inject.Inject

class TvShowsRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val database: TvShowsDatabase,
    private val networkHelper: NetworkHelper
) : TvShowsRepository {

    private val tvShowDao = database.tvShowsDao()

    override suspend fun getTvShowsByPage(page: Int) = networkBoundResource(
        query = {
            tvShowDao.getTvShowsByPage(page)
        },
        fetch = {
            delay(DELAY)
            api.getPopularTvShows(API_KEY, page)
        },
        saveFetchResult = { tvShowsResponse ->
            database.withTransaction {
                tvShowDao.deleteTvShowsByPage(tvShowsResponse.page)
                tvShowDao.insertTvShows(tvShowsResponse.tvShows.map { it.copy(page = tvShowsResponse.page) })
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
            it.message
        }
    )

    override suspend fun getTvShowDetails(tvShowId: Int) = networkBoundResource(
        query = {
            tvShowDao.getTvShowById(tvShowId)
        },
        fetch = {
            delay(DELAY)
            api.getTvShowDetails(tvShowId, API_KEY)
        },
        saveFetchResult = {
            database.withTransaction {
                tvShowDao.deleteTvShowsById(tvShowId)
                tvShowDao.insertTvShow(it)
            }
        },
        shouldFetch = {
            networkHelper.isNetworkAvailable()
        },
        onFetchFailed = {
            it.message
        }
    )

    companion object {
        private const val API_KEY = Constant.API_KEY
        private const val DELAY = 1000L
    }
}
