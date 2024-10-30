package com.dpudov.domain.repository

import com.dpudov.domain.model.Frame
import java.util.UUID

interface IFrameRepository {
    suspend fun loadNextFrames(animationId: UUID, lastFrameId: UUID?, pageSize: Int): List<Frame>

    suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame>

    suspend fun addFrame(frame: Frame)

    suspend fun removeFrame(frame: Frame)
}