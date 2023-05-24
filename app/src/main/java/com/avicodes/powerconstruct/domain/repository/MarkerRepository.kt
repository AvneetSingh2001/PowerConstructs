package com.avicodes.powerconstruct.domain.repository

import com.avicodes.powerconstruct.data.models.Marker
import kotlinx.coroutines.flow.Flow
import com.avicodes.powerconstruct.data.utils.Result


interface MarkerRepository {

    fun getAllMarkers(drawingId: String): Flow<Result<List<Marker>>>

    fun postMarker(marker: Marker): Flow<Result<String>>
}