package com.avicodes.powerconstruct.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Marker(

    var id: String = "",

    var drawingId: String = "",

    var title: String = "",

    var description: String = "",

    val timeCreated: String = "",

    val x: Float = 0f,

    val y: Float = 0f

) : Parcelable