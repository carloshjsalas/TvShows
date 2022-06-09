package com.cs.tvshows.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cs.tvshows.data.model.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowsDao {

    @Query("SELECT * FROM tvshows WHERE page = :numberOfPage")
    fun getTvShowsByPage(numberOfPage: Int): Flow<List<TvShow>>

    @Query("SELECT * FROM tvshows WHERE id = :tvShowId")
    fun getTvShowById(tvShowId: Int): Flow<TvShow>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShows(tvShows: List<TvShow>)

    @Query("DELETE FROM tvshows WHERE page = :numberOfPage")
    suspend fun deleteTvShowsByPage(numberOfPage: Int)

    @Query("DELETE FROM tvshows WHERE id = :tvShowId")
    suspend fun deleteTvShowsById(tvShowId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShow: TvShow)
}
