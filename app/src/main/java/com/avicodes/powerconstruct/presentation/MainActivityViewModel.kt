package com.avicodes.powerconstruct.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import com.avicodes.powerconstruct.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivityViewModel(
    private var drawingRepository: DrawingRepository,
    private var markerRepository: MarkerRepository
) : ViewModel() {

    val markers: MutableLiveData<Result<List<Marker>>> = MutableLiveData(Result.NotInitialized)

    val drawing: MutableLiveData<Result<List<Drawing>>> = MutableLiveData(Result.NotInitialized)

    fun addDrawing(title: String, imgUri: String): Flow<Result<String>> {
        return drawingRepository.postDrawing(
            title = title,
            imgUri = imgUri
        )
    }

    fun getAllDrawings() = viewModelScope.launch {
        drawingRepository.getAllDrawings().collectLatest {
            drawing.postValue(it)
        }
    }


    fun getAllMarkers(drawingId: String) = viewModelScope.launch {
        markerRepository.getAllMarkers(
            drawingId = drawingId
        ).collectLatest {
            markers.postValue(it)
        }
    }

    fun postMarker(marker: Marker): Flow<Result<String>> {
        return markerRepository.postMarker(
            marker = marker
        )
    }
}