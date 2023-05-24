package com.avicodes.powerconstruct.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Drawing(

    var id: String = "",

    var name: String = "",

    val timeAdded: String = "",

    var url: String = "",

    var markerCount: Int = 0

) : Parcelable
