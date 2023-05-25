package com.avicodes.powerconstruct.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import com.avicodes.powerconstruct.domain.repository.MarkerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivityViewModel(
    private var drawingRepository: DrawingRepository,
    private var markerRepository: MarkerRepository
) : ViewModel() {

    val markers: MutableLiveData<Result<List<Marker>>> = MutableLiveData(Result.NotInitialized)
    val markerUploaded: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)

    val drawing: MutableLiveData<Result<List<Drawing>>> = MutableLiveData(Result.NotInitialized)
    val drawingUploaded: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)

    fun addDrawing(title: String, imgUri: String) = viewModelScope.launch {
        drawingRepository.postDrawing(
            title = title,
            imgUri = imgUri
        ).collectLatest {
            drawingUploaded.postValue(it)
        }
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

    fun postMarker(marker: Marker) = viewModelScope.launch {
        markerRepository.postMarker(
            marker = marker
        ).collectLatest {
            markerUploaded.postValue(it)
        }
    }

    fun updateMarkerCount(drawingId: String, markerCount: Int)  = viewModelScope.launch(Dispatchers.IO){
        markerRepository.updateMarkerCount(
            drawingId = drawingId,
            markerCount = markerCount
        )
    }
}