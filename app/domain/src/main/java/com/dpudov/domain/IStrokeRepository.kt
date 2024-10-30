package com.dpudov.domain

import java.util.UUID

interface IStrokeRepository {
    suspend fun addStroke(stroke: Stroke)

    suspend fun removeStroke(strokeId: UUID)
}