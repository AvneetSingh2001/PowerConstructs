package com.avicodes.powerconstruct.presentation.ui.drawing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.databinding.FragmentAddDrawingBinding
import com.avicodes.powerconstruct.presentation.MainActivityViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddDrawingFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddDrawingBinding? = null
    private val binding get() = _binding!!

    val args: AddDrawingFragmentArgs by navArgs()

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUri = args.drawingUri

        showImage(imageUri)

        binding.apply {
            btnUploadDrawing.setOnClickListener {
                val title = tvTitle.editText?.text

                if (!title.isNullOrBlank()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.addDrawing(
                            title = title.toString(),
                            imgUri = imageUri
                        ).collectLatest {
                            withContext(Dispatchers.Main) {
                                validateResponse(it)
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter Title", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun showImage(imageUri: String) {
        binding.apply {
            Glide.with(ivDrawing)
                .load(imageUri)
                .into(ivDrawing)
        }
    }

    private fun validateResponse(it: Result<String>) {
        when (it) {
            is Result.Success -> {
                Toast.makeText(context, "Drawing Added", Toast.LENGTH_SHORT)
                    .show()
                viewModel.getAllDrawings()
                this.dismiss()
            }

            is Result.Error -> {
                Toast.makeText(
                    context,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                requireView().findNavController().popBackStack()
            }

            else -> {}
        }
    }

}