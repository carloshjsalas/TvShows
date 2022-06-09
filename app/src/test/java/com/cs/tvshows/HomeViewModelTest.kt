package com.cs.tvshows

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.data.model.TvShowsListResponse
import com.cs.tvshows.data.source.local.TvShowsDao
import com.cs.tvshows.data.source.local.TvShowsDatabase
import com.cs.tvshows.data.source.remote.ApiService
import com.cs.tvshows.data.source.repository.TvShowsRepository
import com.cs.tvshows.data.source.repository.TvShowsRepositoryImpl
import com.cs.tvshows.ui.home.HomeActions
import com.cs.tvshows.ui.home.HomeViewModel
import com.cs.tvshows.utils.NetworkHelper
import com.cs.tvshows.utils.Outcome
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
open class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var tvShowsRepository: TvShowsRepository

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var tvShowsList: List<TvShow>

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        homeViewModel = HomeViewModel(tvShowsRepository)
        tvShowsList = listOf(
            TvShow(
                ID,
                VOTE_AVERAGE,
                TITLE,
                NAME,
                POPULARITY,
                POSTER_PATH,
                BACKDROP_PATH,
                OVERVIEW,
                RELEASE_DATE,
                FIRST_AIR_DATE,
                PAGE
            )
        )
    }

    @Test
    fun `getTvShowsFirstPage SHOULD no present data before reach the repository at the very first time`() =
        runTest {
            homeViewModel.getTvShowsFirstPage()

            Assert.assertEquals(
                null,
                homeViewModel.tvShows.value?.data
            )
        }

    @Test
    fun `getTvShowsFirstPage SHOULD return a list of tv shows after reach the repository`() =
        runTest {
            val response = Outcome.Success(tvShowsList)
            val channel = Channel<Outcome<List<TvShow>>>()
            val flow = channel.consumeAsFlow()

            Mockito.`when`(tvShowsRepository.getTvShowsByPage(PAGE)).thenReturn(flow)

            launch { channel.send(response) }

            homeViewModel.getTvShowsFirstPage()

            delay(DELAY)

            Assert.assertEquals(
                tvShowsList,
                (homeViewModel.tvShows.value?.data as HomeActions.OnTvShowsFirstPageLoaded).items
            )
        }

    companion object {
        const val ID = 1234
        const val VOTE_AVERAGE = 8.4
        const val TITLE = "The Boys"
        const val NAME = "The Boys"
        const val POPULARITY = 228.4
        const val POSTER_PATH = "/stTEycfG9928HYGEISBFaG1ngjM.jpg"
        const val BACKDROP_PATH = "/stiQsL31rO4uSksWWSUBU5EdKon.jpg"
        const val OVERVIEW = "A group of vigilantes known informally as The Boys set out"
        const val RELEASE_DATE = "2019-07-25"
        const val FIRST_AIR_DATE = "2019-07-25"
        const val PAGE = 1
        const val DELAY = 1000L
    }
}
