package com.cs.tvshows.di

import android.content.Context
import androidx.room.Room
import com.cs.tvshows.data.source.local.TvShowsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TvShowsDatabase =
        Room.databaseBuilder(
            context, TvShowsDatabase::class.java,
            "tv_shows_db"
        )
            .fallbackToDestructiveMigration()
            .build()
}
