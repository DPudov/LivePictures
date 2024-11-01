package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Point
import com.dpudov.frames.datasource.local.entity.PointEntity

fun Point.toEntity(): PointEntity = PointEntity(
    strokeId = strokeId,
    x = x,
    y = y
)

fun PointEntity.toData(): Point = Point(
    id = id,
    strokeId = strokeId,
    x = x,
    y = y
)