package com.avicodes.powerconstruct.presentation.ui.marker.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.avicodes.powerconstruct.data.utils.TimeCalc
import com.avicodes.powerconstruct.databinding.FragmentShowMarkerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ShowMarkerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShowMarkerBinding? = null
    private val binding get() = _binding!!

    val args: ShowMarkerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowMarkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val marker = args.marker

        binding.apply {
            tvTime.text = TimeCalc.getTimeAgo(marker.timeCreated.toLong())
            tvTitle.text = marker.title
            tvDesc.text = marker.description
        }
    }

}