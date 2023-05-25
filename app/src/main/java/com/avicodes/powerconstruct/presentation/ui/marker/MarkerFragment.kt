package com.avicodes.powerconstruct.presentation.ui.marker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.powerconstruct.R
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.databinding.FragmentMarkerBinding
import com.avicodes.powerconstruct.presentation.MainActivityViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.presentation.ui.marker.MarkerFragmentArgs


class MarkerFragment : Fragment() {

    private var _binding: FragmentMarkerBinding? = null
    private val binding get() = _binding!!

    val viewModel by activityViewModels<MainActivityViewModel>()
    val args: MarkerFragmentArgs by navArgs()

    private lateinit var drawing: Drawing

    var markersList = listOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawing = args.drawing

        viewModel.getAllMarkers(drawingId = drawing.id)

        observeMarkerUploaded()

        binding.showMarkersButton.setOnClickListener {
            routeToShowAllMarkers()
        }



        Glide.with(binding.ivDrawing)
            .load(drawing.url)
            .into(binding.ivDrawing)
    }




    private fun observeMarkerUploaded() {
        viewModel.markerUploaded.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context, "Marker Added", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getAllMarkers(drawingId = drawing.id)
                    viewModel.updateMarkerCount(
                        drawingId = drawing.id,
                        markerCount = drawing.markerCount.plus(1)
                    )
                    viewModel.markerUploaded.postValue(Result.NotInitialized)
                }

                is Result.Error -> {
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        })
    }


    private fun routeToShowAllMarkers() {
        val action = MarkerFragmentDirections.actionMarkerFragmentToAllMarkersFragment()
        requireView().findNavController().navigate(action)
    }
}