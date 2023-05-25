package com.avicodes.powerconstruct.presentation.ui.drawing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.databinding.FragmentDrawingBinding
import kotlinx.coroutines.launch
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.presentation.MainActivityViewModel
import com.avicodes.powerconstruct.presentation.ui.drawing.adapter.DrawingAdapter


class DrawingFragment : Fragment() {

    private var _binding: FragmentDrawingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainActivityViewModel>()

    private lateinit var adapter: DrawingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DrawingAdapter()

        initDrawingRecyclerView()

        lifecycleScope.launch {
            viewModel.getAllDrawings()

            viewModel.drawing.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        hideProg()
                        adapter.differ.submitList(it.data)
                    }

                    is Result.Loading -> {
                        showProg()
                    }

                    is Result.Error -> {
                        hideProg()
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                    is Result.NotInitialized -> {}
                }
            })
        }

        adapter.setOnItemClickListener {
            routeToMarkerScreen(drawing = it)
        }

        binding.apply {
            btnAddDrawing.setOnClickListener {
                pickImage()
            }
        }

        observeDrawingUploaded()
    }

    private fun observeDrawingUploaded() {
        viewModel.drawingUploaded.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    hideProg()
                    Toast.makeText(context, "Drawing Added", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getAllDrawings()
                    viewModel.drawingUploaded.postValue(Result.NotInitialized)
                }

                is Result.Error -> {
                    hideProg()
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    showProg()
                }
            }
        })
    }


    fun pickImage() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            selectPictureLauncher.launch(type)
        }
    }


    private val selectPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                routeToAddDrawingScreen(uri)
            }
        }

    private fun routeToMarkerScreen(drawing: Drawing) {
    }

    private fun routeToAddDrawingScreen(drawingUri: Uri?) {
        val action =
            DrawingFragmentDirections.actionDrawingFragmentToAddDrawingFragment(drawingUri.toString())
        requireView().findNavController().navigate(action)
    }


    private fun initDrawingRecyclerView() {
        binding.apply {
            rvDrawings.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvDrawings.adapter = adapter
        }
    }

    private fun showProg() {
        binding.apply {
            clProg.visibility = View.VISIBLE
            clMain.visibility = View.INVISIBLE
        }
    }


    private fun hideProg() {
        binding.apply {
            clProg.visibility = View.GONE
            clMain.visibility = View.VISIBLE
        }
    }
}