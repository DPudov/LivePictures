package com.dpudov.data

import com.dpudov.domain.model.Frame
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface IFrameDaoService {
    fun loadAnyByIds(ids: List<UUID>): Flow<List<Frame>>

    suspend fun loadNextFrames(animationId: UUID, lastFrameId: UUID?, pageSize: Int): List<Frame>

    suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame>

    suspend fun loadLastFrame(animationId: UUID): Frame?

    suspend fun loadFirstFrame(animationId: UUID): Frame?

    suspend fun loadNext(nextId: UUID): Frame?

    suspend fun loadPrev(prevId: UUID): Frame?

    suspend fun loadById(id: UUID): Frame?

    suspend fun loadById(animationId: UUID, id: UUID): Frame?

    suspend fun addFrame(frame: Frame)

    suspend fun removeFrame(frame: Frame)

    suspend fun removeByAnimation(animationId: UUID)
}