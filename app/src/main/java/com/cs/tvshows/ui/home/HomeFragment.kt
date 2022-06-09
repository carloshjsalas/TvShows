package com.cs.tvshows.ui.home

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.tvshows.R
import com.cs.tvshows.data.model.TvShow
import com.cs.tvshows.databinding.FragmentHomeBinding
import com.cs.tvshows.protocol.CommunicationCallback
import com.cs.tvshows.protocol.ProtocolAction
import com.cs.tvshows.ui.MainActivity
import com.cs.tvshows.utils.Outcome
import com.cs.tvshows.utils.gone
import com.cs.tvshows.utils.onScrolledToEnd
import com.cs.tvshows.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), TvShowsHomeAdapter.ItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var communication: CommunicationCallback

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communication = (requireActivity() as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeToRefresh()
        setupMainViewModelObserver()
        viewModel.getTvShowsFirstPage()
    }

    override fun onItemClicked(item: TvShow) {
        communication.onFragmentEvent(ProtocolAction.OnOpenTvShowDetails(item.id))
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getTvShowsFirstPage()
            }
        }
    }

    private fun setupMainViewModelObserver() {
        binding.apply {
            viewModel.tvShows.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Outcome.Loading -> {
                        if (!swipeRefreshLayout.isRefreshing) {
                            progressBar.visible()
                        }
                    }
                    is Outcome.Success -> {
                        progressBar.gone()
                        swipeRefreshLayout.isRefreshing = false
                        handleSuccess(result.data)
                    }
                    is Outcome.Error -> {
                        progressBar.gone()
                        swipeRefreshLayout.isRefreshing = false
                        showErrorMessage(result.error?.message)
                    }
                }
            }
        }
    }

    private fun handleSuccess(homeActions: HomeActions?) {
        when (homeActions) {
            is HomeActions.OnTvShowsFirstPageLoaded -> {
                setupTvShowList(homeActions.items)
            }
            is HomeActions.OnTvShowsNextPageLoaded -> {
                addTvShowsNextPage(homeActions.items)
            }
            else -> {
                showErrorMessage()
            }
        }
    }

    private fun setupTvShowList(tvShows: List<TvShow>) {
        binding.tvShowsList.apply {
            adapter = TvShowsHomeAdapter(tvShows.toMutableList(), this@HomeFragment).apply {
                submitList(tvShows)
            }
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            itemAnimator = null
            onScrolledToEnd { viewModel.loadNextPage() }
        }
    }

    private fun addTvShowsNextPage(tvShows: List<TvShow>) {
        (binding.tvShowsList.adapter as? TvShowsHomeAdapter)?.addTvShows(tvShows)
    }

    private fun showErrorMessage(message: String? = getString(R.string.error_message)) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
