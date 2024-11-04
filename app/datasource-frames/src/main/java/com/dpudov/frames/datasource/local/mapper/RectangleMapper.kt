package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Rect
import com.dpudov.frames.datasource.local.entity.RectangleEntity

fun RectangleEntity.toData(): Rect = Rect(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    topLeftX = topLeftX,
    topLeftY = topLeftY,
    width = width,
    height = height
)

fun Rect.toEntity(): RectangleEntity = RectangleEntity(
    id = id,
    frameId = frameId,
    finishTimestamp = finishTimestamp,
    color = color,
    thickness = thickness,
    topLeftX = topLeftX,
    topLeftY = topLeftY,
    width = width,
    height = height
)