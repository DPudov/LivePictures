package com.dpudov.data

import com.dpudov.domain.model.Stroke
import java.util.UUID

interface IStrokeDaoService {
    suspend fun addStroke(stroke: Stroke)

    suspend fun removeStroke(strokeId: UUID)
}