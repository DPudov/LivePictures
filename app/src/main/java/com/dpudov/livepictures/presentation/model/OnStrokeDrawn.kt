package com.dpudov.livepictures.presentation.model

import android.graphics.PointF

fun interface OnStrokeDrawn {
    fun onStrokeDrawn(points: List<PointF>, tool: Tool, color: Int, thickness: Float)
}