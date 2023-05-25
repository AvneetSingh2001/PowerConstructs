package com.avicodes.powerconstruct.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import com.avicodes.powerconstruct.domain.repository.MarkerRepository

class MainActivityViewModelFactory(
    private val drawingRepository: DrawingRepository,
    private val markerRepository: MarkerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            drawingRepository = drawingRepository,
            markerRepository = markerRepository
        ) as T
    }
}