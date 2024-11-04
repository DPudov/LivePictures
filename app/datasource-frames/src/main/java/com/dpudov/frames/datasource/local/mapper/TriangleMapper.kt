package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Triangle
import com.dpudov.frames.datasource.local.entity.TriangleEntity

fun TriangleEntity.toData(): Triangle = Triangle(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    x1 = x1,
    y1 = y1,
    x2 = x2,
    y2 = y2,
    x3 = x3,
    y3 = y3
)

fun Triangle.toEntity(): TriangleEntity = TriangleEntity(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    x1 = x1,
    y1 = y1,
    x2 = x2,
    y2 = y2,
    x3 = x3,
    y3 = y3
)