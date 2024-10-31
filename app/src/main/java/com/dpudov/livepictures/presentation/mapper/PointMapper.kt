package com.dpudov.livepictures.presentation.mapper

import android.graphics.PointF
import com.dpudov.domain.model.Point
import java.util.UUID

fun Point.toViewData(): PointF = PointF(x, y)

fun PointF.toData(strokeId: UUID): Point = Point(
    id = 0L,
    strokeId = strokeId,
    x = x,
    y = y
)