package com.cs.tvshows.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.data.source.repository.TvShowsRepository
import com.cs.tvshows.utils.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    val tvShows by lazy { MutableLiveData<Outcome<HomeActions>>() }

    private var page = 1
    private var isLoadingNextPage = false

    fun getTvShowsFirstPage() {
        page = 1
        isLoadingNextPage = true
        tvShows.value = Outcome.Loading()
        getTvShowsByPage()
    }

    fun loadNextPage() {
        if (!isLoadingNextPage) {
            page++
            isLoadingNextPage = true
            tvShows.value = Outcome.Loading()
            getTvShowsByPage()
        }
    }

    private fun getTvShowsByPage() = viewModelScope.launch {
        repository.getTvShowsByPage(page).collect {
            when (it) {
                is Outcome.Success -> {
                    isLoadingNextPage = false
                    handleSuccess(it.data)
                }
                is Outcome.Error -> {
                    isLoadingNextPage = false
                    tvShows.value = Outcome.Error(it.error)
                }
                is Outcome.Loading -> {
                    isLoadingNextPage = true
                    tvShows.value = Outcome.Loading()
                }
            }
        }
    }

    private fun handleSuccess(data: List<TvShow>?) {
        data?.let { tvShowsList ->
            if (page == 1 && tvShowsList.firstOrNull()?.page == 1) {
                tvShows.value =
                    Outcome.Success(HomeActions.OnTvShowsFirstPageLoaded(tvShowsList))
            } else if (tvShowsList.firstOrNull()?.page == page) {
                tvShows.value =
                    Outcome.Success(HomeActions.OnTvShowsNextPageLoaded(tvShowsList))
            }
        } ?: run {
            isLoadingNextPage = false
            tvShows.value = Outcome.Error()
        }
    }
}
