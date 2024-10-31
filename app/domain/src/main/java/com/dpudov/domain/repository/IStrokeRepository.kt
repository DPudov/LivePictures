package com.dpudov.domain.repository

import com.dpudov.domain.model.Stroke
import java.util.UUID

interface IStrokeRepository {
    suspend fun getStrokesByFrameId(frameId: UUID): List<Stroke>

    suspend fun addStroke(stroke: Stroke)

    suspend fun removeStroke(strokeId: UUID)
}