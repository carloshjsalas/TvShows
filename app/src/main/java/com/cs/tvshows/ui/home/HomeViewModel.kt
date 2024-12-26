package com.cs.tvshows.ui.home

import androidx.lifecycle.LiveData
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

    private val _tvShows by lazy { MutableLiveData<Outcome<HomeActions>>() }
    val tvShows: LiveData<Outcome<HomeActions>> get() = _tvShows


    private var page = 1
    private var isLoadingNextPage = false

    fun getTvShowsFirstPage() {
        page = 1
        isLoadingNextPage = true
        _tvShows.value = Outcome.Loading()
        getTvShowsByPage()
    }

    fun loadNextPage() {
        if (!isLoadingNextPage) {
            page++
            isLoadingNextPage = true
            _tvShows.value = Outcome.Loading()
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
                    _tvShows.value = Outcome.Error(it.error)
                }
                is Outcome.Loading -> {
                    isLoadingNextPage = true
                    _tvShows.value = Outcome.Loading()
                }
            }
        }
    }

    private fun handleSuccess(data: List<TvShow>?) {
        data?.let { tvShowsList ->
            if (page == 1 && tvShowsList.firstOrNull()?.page == 1) {
                _tvShows.value =
                    Outcome.Success(HomeActions.OnTvShowsFirstPageLoaded(tvShowsList))
            } else if (tvShowsList.firstOrNull()?.page == page) {
                _tvShows.value =
                    Outcome.Success(HomeActions.OnTvShowsNextPageLoaded(tvShowsList))
            }
        } ?: run {
            isLoadingNextPage = false
            _tvShows.value = Outcome.Error()
        }
    }
}
