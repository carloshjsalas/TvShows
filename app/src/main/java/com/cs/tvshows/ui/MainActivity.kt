package com.cs.tvshows.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cs.tvshows.R
import com.cs.tvshows.databinding.ActivityMainBinding
import com.cs.tvshows.protocol.CommunicationCallback
import com.cs.tvshows.protocol.ProtocolAction
import com.cs.tvshows.ui.details.DetailsFragment.Companion.TV_SHOW_ID
import com.cs.tvshows.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CommunicationCallback {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment)
    }

    override fun onFragmentEvent(action: ProtocolAction) {
        when (action) {
            is ProtocolAction.OnOpenTvShowDetails -> {
                openTvShowDetails(action.tvShowId)
            }
        }
    }

    private fun openTvShowDetails(tvShowId: Int) {
        val bundle = bundleOf(TV_SHOW_ID to tvShowId)
        navController.navigate(R.id.homeToDetailDestination, bundle)
    }
}
