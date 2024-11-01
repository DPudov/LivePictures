package com.dpudov.data

import com.dpudov.domain.model.Stroke
import java.util.UUID

interface IStrokeDaoService {
    suspend fun getStrokesByFrame(frameId: UUID): List<Stroke>

    suspend fun addStroke(stroke: Stroke)

    suspend fun removeStroke(strokeId: UUID)
}