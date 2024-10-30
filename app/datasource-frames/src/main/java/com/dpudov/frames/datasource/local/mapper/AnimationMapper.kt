package com.dpudov.frames.datasource.local.mapper

import com.dpudov.domain.model.Animation
import com.dpudov.frames.datasource.local.entity.AnimationEntity

fun AnimationEntity.toData(): Animation = Animation(
    id = id,
    name = name,
    createdAt = createdAt
)

fun Animation.toEntity(): AnimationEntity = AnimationEntity(
    id = id,
    name = name,
    createdAt = createdAt
)
