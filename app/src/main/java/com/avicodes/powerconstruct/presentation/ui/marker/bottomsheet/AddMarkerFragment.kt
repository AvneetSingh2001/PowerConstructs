package com.avicodes.powerconstruct.presentation.ui.marker.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.databinding.FragmentAddMarkerBinding
import com.avicodes.powerconstruct.presentation.MainActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


class AddMarkerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddMarkerBinding? = null
    private val binding get() = _binding!!

    val args: AddMarkerFragmentArgs by navArgs()

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddMarkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = args.x
        val y = args.y
        val drawingId = args.drawingId
        val markerCount = args.markerCount

        binding.apply {

            btnAddMarker.setOnClickListener {
                val title = tvTitle.editText?.text
                val desc = tvDesc.editText?.text

                if (title.isNullOrBlank()) {
                    Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                } else if (desc.isNullOrBlank()) {
                    Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                } else {

                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.postMarker(
                            Marker(
                                id = UUID.randomUUID().toString(),
                                drawingId = drawingId,
                                title = title.toString(),
                                description = desc.toString(),
                                timeCreated = System.currentTimeMillis().toString(),
                                x = x,
                                y = y
                            )
                        )
                        dismiss()
                    }
                }
            }

        }
    }

}