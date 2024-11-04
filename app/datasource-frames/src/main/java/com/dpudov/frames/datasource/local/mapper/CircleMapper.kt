package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Circle
import com.dpudov.frames.datasource.local.entity.CircleEntity

fun CircleEntity.toData(): Circle = Circle(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    radius = radius,
    centerX = centerX,
    centerY = centerY
)

fun Circle.toEntity(): CircleEntity = CircleEntity(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    radius = radius,
    centerX = centerX,
    centerY = centerY
)