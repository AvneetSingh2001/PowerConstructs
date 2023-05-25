package com.avicodes.powerconstruct.presentation.ui.marker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.avicodes.powerconstruct.R
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlin.math.min

class PinView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null
) : SubsamplingScaleImageView(context, attr) {

    private val radius: Float
    private var marker: Bitmap
    private val paint = Paint()
    private val pointF = PointF()

    var markers = mutableListOf<PointF>()
        set(value) {
            field = value
            invalidate()
        }

    init {
        val density = resources.displayMetrics.densityDpi.toFloat()
        val bitmap = ContextCompat.getDrawable(context, R.drawable.ic_anchor_36dp)?.toBitmap()!!
        val w = density / 420f * bitmap.width
        val h = density / 420f * bitmap.height
        radius = min(w, h) / 4
        marker = Bitmap.createScaledBitmap(bitmap, w.toInt(), h.toInt(), true)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.isAntiAlias = true
        markers.forEach {
            sourceToViewCoord(it, pointF)
            val vX = pointF.x - marker.width / 2
            val vY = pointF.y - marker.height
            canvas?.drawBitmap(marker, vX, vY, paint)
        }
    }

    fun mark(pointF: PointF) {
        markers.plusAssign(pointF)
        invalidate()
    }

    fun isInside(center: PointF, marker: PointF): Boolean {
        return (marker.x - center.x) * (marker.x - center.x) + (marker.y - center.y) * (marker.y - center.y) <= radius * radius
    }

}
