package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.Frame
import com.dpudov.frames.datasource.local.entity.FrameEntity

fun FrameEntity.toData(): Frame = Frame(
    id = id,
    animationId = animationId,
    prevId = prevFrameId,
    nextId = nextFrameId
)

fun Frame.toEntity(): FrameEntity = FrameEntity(
    id = id,
    animationId = animationId,
    prevFrameId = prevId,
    nextFrameId = nextId
)
