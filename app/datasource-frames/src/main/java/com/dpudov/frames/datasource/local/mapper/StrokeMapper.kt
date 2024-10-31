package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Stroke
import com.dpudov.frames.datasource.local.entity.PointEntity
import com.dpudov.frames.datasource.local.entity.StrokeEntity
import com.dpudov.frames.datasource.local.entity.StrokeWithPoints

fun StrokeEntity.toData(points: List<PointEntity>): Stroke = Stroke(
    id = id,
    frameId = frameId,
    color = color,
    thickness = thickness,
    instrument = instrument.toData(),
    finishTimestamp = finishTimestamp,
    points = points.map(PointEntity::toData)
)

fun StrokeWithPoints.toData(): Stroke = Stroke(
    id = stroke.id,
    frameId = stroke.frameId,
    color = stroke.color,
    thickness = stroke.thickness,
    instrument = stroke.instrument.toData(),
    finishTimestamp = stroke.finishTimestamp,
    points = points.map(PointEntity::toData)
)

fun Stroke.toEntity(): StrokeEntity = StrokeEntity(
    id = id,
    frameId = frameId,
    color = color,
    thickness = thickness,
    instrument = instrument.toEnum(),
    finishTimestamp = finishTimestamp
)