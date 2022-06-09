package com.cs.tvshows.ui.details

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
class DetailsViewModel @Inject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    val tvShowOutcome by lazy { MutableLiveData<Outcome<DetailsActions>>() }

    fun getTvShow(tvShowId: Int) = viewModelScope.launch {
        repository.getTvShowDetails(tvShowId).collect {
            when (it) {
                is Outcome.Success -> {
                    handleSuccess(it.data)
                }
                is Outcome.Error -> {
                    tvShowOutcome.value = Outcome.Error(it.error)
                }
                is Outcome.Loading -> {
                    tvShowOutcome.value = Outcome.Loading()
                }
            }
        }
    }

    private fun handleSuccess(data: TvShow?) {
        data?.let { tvShow ->
            tvShowOutcome.value = Outcome.Success(DetailsActions.OnTvShowLoaded(tvShow))
        } ?: run {
            tvShowOutcome.value = Outcome.Error()
        }
    }
}
