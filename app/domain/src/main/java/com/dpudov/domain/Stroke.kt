package com.dpudov.domain

data class Stroke(
    val color: Int,
    val thickness: Float,
    val points: List<PointF>
) : DrawableItem
