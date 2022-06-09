package com.cs.tvshows.ui.details

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cs.tvshows.R
import com.cs.tvshows.databinding.FragmentDetailsBinding
import com.cs.tvshows.utils.Outcome
import com.cs.tvshows.utils.gone
import com.cs.tvshows.utils.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailsBinding

    private val tvShowId by lazy { requireArguments().getInt(TV_SHOW_ID) }
    private val viewModel by viewModels<DetailsViewModel>()

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let { layout ->
                    val behavior = BottomSheetBehavior.from(layout)
                    behavior.skipCollapsed = true
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    val layoutParams = layout.layoutParams
                    layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
                    layout.layoutParams = layoutParams
                }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupMainViewModelObserver()
        getTvShowDetail()
    }

    private fun setupUI() {
        binding.imageViewClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun setupMainViewModelObserver() {
        binding.apply {
            viewModel.tvShowOutcome.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Outcome.Loading -> {
                        cardViewInfo.gone()
                        progressBar.visible()
                    }
                    is Outcome.Success -> {
                        progressBar.gone()
                        cardViewInfo.visible()
                        result.data?.let { action ->
                            if (action is DetailsActions.OnTvShowLoaded) {
                                binding.tvShow = action.item
                            }
                        }
                    }
                    is Outcome.Error -> {
                        progressBar.gone()
                        showErrorMessage(result.error?.message)
                    }
                }
            }
        }
    }

    private fun getTvShowDetail() {
        viewModel.getTvShow(tvShowId)
    }

    private fun showErrorMessage(message: String? = getString(R.string.error_message)) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TV_SHOW_ID = "tv_show_id"
    }
}
