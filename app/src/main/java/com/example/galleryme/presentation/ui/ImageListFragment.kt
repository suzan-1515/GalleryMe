package com.example.galleryme.presentation.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galleryme.R
import com.example.galleryme.databinding.FragmentImageListBinding
import com.example.galleryme.presentation.adapter.ImageListAdapter
import com.example.galleryme.presentation.viewmodel.ImageViewModel
import com.example.galleryme.presentation.viewmodel.UiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ImageListFragment : Fragment() {

    private var _binding: FragmentImageListBinding? = null
    private val imageAdapter by lazy { ImageListAdapter() }

    private val binding get() = _binding!!

    private val viewModel: ImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        AuthorListDialogFragment.newInstance()
                            .show(childFragmentManager, "author_list")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {
            imageListRecyclerView.apply {
                layoutManager = GridLayoutManager(
                    requireContext(),
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2
                )
                adapter = imageAdapter
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState
                    .map {
                        when (it) {
                            is UiState.Loading -> renderLoading(it)
                            is UiState.Success -> renderSuccess(it)
                            is UiState.Error -> renderError(it)
                        }
                    }
                    .collect { }
            }
        }
        viewModel.currentAuthor.observe(viewLifecycleOwner) { author ->
            if (author != null && author.isNotEmpty()) {
                binding.apply {
                    filterTermTextView.visibility = View.VISIBLE
                    filterTermTextView.text = getString(R.string.showing_images_for, author)
                }

            } else {
                binding.apply {
                    filterTermTextView.visibility = View.GONE
                }
            }
        }

    }

    private fun renderSuccess(state: UiState.Success): UiState {
        binding.apply {
            imageListRecyclerView.visibility = View.VISIBLE
            if (state.data.isEmpty()) {
                messageLayout.visibility = View.VISIBLE
                messageTextView.text = getString(R.string.empty_data)
                retryButton.setOnClickListener { viewModel.getImages() }
            } else {
                messageLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                imageAdapter.differ.submitList(state.data)
            }
        }
        return state
    }

    private fun renderError(state: UiState.Error): UiState {
        binding.apply {
            progressBar.visibility = View.GONE
        }
        if (state.data != null && state.data.isNotEmpty()) {
            state.message?.let { message ->
                showSnackBar(message)
            }
        } else {
            binding.apply {
                imageListRecyclerView.visibility = View.INVISIBLE
                messageLayout.visibility = View.VISIBLE
                messageTextView.text = state.message
                retryButton.setOnClickListener { viewModel.getImages() }
            }
        }

        return state
    }

    private fun renderLoading(state: UiState.Loading): UiState {
        if (state.data != null) {
            binding.apply {
                imageListRecyclerView.visibility = View.VISIBLE
                imageAdapter.differ.submitList(state.data)
            }
        }
        binding.apply {
            progressBar.visibility = View.VISIBLE
            messageLayout.visibility = View.GONE
        }

        return state
    }

    private fun showSnackBar(message: String) {
        val snackBar: Snackbar = Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                viewModel.getImages()
            }

        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}