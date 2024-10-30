package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Stroke
import com.dpudov.frames.datasource.local.entity.StrokeEntity

fun StrokeEntity.toData(): Stroke = Stroke(
    id = id,
    frameId = frameId,
    color = color,
    thickness = thickness,
    instrumentId = instrumentId,
    finishTimestamp = finishTimestamp
)

fun Stroke.toEntity(): StrokeEntity = StrokeEntity(
    id = id,
    frameId = frameId,
    color = color,
    thickness = thickness,
    instrumentId = instrumentId,
    finishTimestamp = finishTimestamp
)