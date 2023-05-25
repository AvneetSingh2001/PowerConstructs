package com.avicodes.powerconstruct.presentation.ui.marker.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.powerconstruct.databinding.FragmentAllMarkersBinding
import com.avicodes.powerconstruct.presentation.ui.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.presentation.ui.marker.adapter.MarkerAdapter


class AllMarkersFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentAllMarkersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MarkerAdapter

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllMarkersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MarkerAdapter()

        initRecyclerView()

        viewModel.markers.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Success -> {
                    it.data?.let {
                        adapter.differ.submitList(it)
                    }
                }
                 else -> {}
            }
        })
    }

    private fun initRecyclerView() {
        binding.apply {
            rvMarkers.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvMarkers.adapter = adapter
        }
    }

}