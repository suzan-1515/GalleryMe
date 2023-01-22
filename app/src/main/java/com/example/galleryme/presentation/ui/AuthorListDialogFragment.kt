package com.example.galleryme.presentation.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.galleryme.databinding.FragmentAuthorListDialogBinding
import com.example.galleryme.presentation.adapter.AuthorListAdapter
import com.example.galleryme.presentation.viewmodel.ImageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch


class AuthorListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAuthorListDialogBinding? = null

    private val binding get() = _binding!!

    private val imageViewModel: ImageViewModel by activityViewModels()
    private val authorAdapter by lazy { AuthorListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthorListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authorAdapter.onItemClick = { author ->
            imageViewModel.setAuthor(author)
            dismiss()
        }

        binding.apply {
            authorSpinner.apply {
                imageViewModel.authors.value?.let {
                    val authors = it.toMutableList()
                    if (it.isNotEmpty()) {
                        authors.add(0, "All")
                    }
                    adapter = object : ArrayAdapter<String>(
                        requireContext(),
                        R.layout.simple_spinner_dropdown_item,
                        authors
                    ) {
                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View? {
                            var v: View? = null
                            if (position == 0) {
                                val tv = TextView(context)
                                tv.height = 0
                                tv.visibility = View.GONE
                                v = tv
                            } else {
                                v = super.getDropDownView(position, null, parent)
                            }
                            return v!!
                        }
                    }

                    imageViewModel.currentAuthor.value?.let { author ->
                        if (author.isNotEmpty())
                            setSelection(it.indexOf(author) + 1, true)
                    }
                }
                onItemSelectedListener =
                    object : OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            if (position != 0) {
                                dismiss()
                                imageViewModel.setAuthor(parent.getItemAtPosition(position) as String)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Another interface callback
                        }
                    }
            }
            clearAuthorFilterButton.setOnClickListener {
                imageViewModel.setAuthor("")
                dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                imageViewModel.authors.observe(viewLifecycleOwner) { authors ->
                    authorAdapter.differ.submitList(authors)
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
        ): AuthorListDialogFragment =
            AuthorListDialogFragment()
    }
}