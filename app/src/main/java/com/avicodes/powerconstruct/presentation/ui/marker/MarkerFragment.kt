package com.avicodes.powerconstruct.presentation.ui.marker

import android.annotation.SuppressLint
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
import com.davemorrissey.labs.subscaleview.ImageSource
import com.avicodes.powerconstruct.data.utils.Result


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

        initMarkers()
        observeMarkers()
        observeMarkerUploaded()

        binding.showMarkersButton.setOnClickListener {
            routeToShowAllMarkers()
        }
    }

    private fun observeMarkers() {
        viewModel.markers.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    it.data?.let { list ->
                        markersList = list
                        Log.e("Avneet", markersList.toString())
                        binding.ivDrawing.markers = list.map { marker ->
                            PointF(marker.x, marker.y)
                        }.toMutableList() ?: mutableListOf()
                    }
                }

                is Result.Error -> {
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                }
            }
        })
    }


    private fun initMarkers() {
        binding.apply {

            val gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                        ivDrawing.viewToSourceCoord(e.x, e.y)?.let { center ->
                            for (marker in markersList) {
                                if (ivDrawing.isInside(
                                        center,
                                        PointF(marker.x, marker.y)
                                    )
                                ) {
                                    routeToShowMarker(marker)
                                    break
                                }
                            }
                        }
                        return e != null
                    }

                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        ivDrawing.viewToSourceCoord(e.x, e.y)?.let {
                            routeToAddMarker(
                                it.x,
                                it.y,
                                drawingId = drawing.id,
                                markerCount = drawing.markerCount
                            )
                        }
                        return true
                    }

                })

            Glide.with(ivDrawing.context)
                .asBitmap()
                .load(drawing.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        ivDrawing.setImage(ImageSource.bitmap(resource))
                        binding.ivDrawing.setOnTouchListener { view, motionEvent ->
                            view.performClick()
                            gestureDetector.onTouchEvent(motionEvent)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

        }
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

    fun routeToShowMarker(marker: Marker) {
        val action = MarkerFragmentDirections.actionMarkerFragmentToShowMarkerFragment(
            marker = marker
        )
        requireView().findNavController().navigate(action)
    }

    fun routeToAddMarker(x: Float, y: Float, drawingId: String, markerCount: Int) {
        val action = MarkerFragmentDirections.actionMarkerFragmentToAddMarkerFragment(
            x = x,
            y = y,
            drawingId = drawingId,
            markerCount = markerCount
        )
        requireView().findNavController().navigate(action)
    }

    private fun routeToShowAllMarkers() {
        val action = MarkerFragmentDirections.actionMarkerFragmentToAllMarkersFragment()
        requireView().findNavController().navigate(action)
    }
}