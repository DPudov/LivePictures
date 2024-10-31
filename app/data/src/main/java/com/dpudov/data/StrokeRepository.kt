package com.dpudov.data

import com.dpudov.domain.model.Stroke
import com.dpudov.domain.repository.IStrokeRepository
import java.util.UUID

class StrokeRepository(
    private val localDaoService: IStrokeDaoService
) : IStrokeRepository {
    override suspend fun getStrokesByFrameId(frameId: UUID): List<Stroke> =
        localDaoService.getStrokesByFrame(frameId)

    override suspend fun addStroke(stroke: Stroke) {
        localDaoService.addStroke(stroke)
    }

    override suspend fun removeStroke(strokeId: UUID) {
        localDaoService.removeStroke(strokeId)
    }
}