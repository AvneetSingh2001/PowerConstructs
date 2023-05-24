package com.avicodes.powerconstruct.domain.repository

import com.avicodes.powerconstruct.data.models.Drawing
import kotlinx.coroutines.flow.Flow
import com.avicodes.powerconstruct.data.utils.Result


interface DrawingRepository {
    fun getAllDrawings(): Flow<Result<List<Drawing>>>
    fun postDrawing(title: String, imgUri: String): Flow<Result<String>>
}