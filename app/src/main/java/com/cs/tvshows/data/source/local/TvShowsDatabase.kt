package com.cs.tvshows.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cs.tvshows.data.model.TvShow

@Database(entities = [TvShow::class], version = 1)
abstract class TvShowsDatabase : RoomDatabase() {
    abstract fun tvShowsDao(): TvShowsDao
}
