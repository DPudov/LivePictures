package com.dpudov.data

import com.dpudov.domain.model.Frame
import com.dpudov.domain.repository.IFrameRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FrameRepository(
    private val localDaoService: IFrameDaoService
) : IFrameRepository {
    override fun loadAnyByIds(ids: List<UUID>): Flow<List<Frame>> =
        localDaoService.loadAnyByIds(ids)

    override suspend fun loadNextFrames(
        animationId: UUID,
        lastFrameId: UUID?,
        pageSize: Int
    ): List<Frame> = localDaoService.loadNextFrames(
        animationId = animationId,
        lastFrameId = lastFrameId,
        pageSize = pageSize
    )

    override suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame> = localDaoService.loadPreviousFrames(
        animationId = animationId,
        firstFrameId = firstFrameId,
        pageSize = pageSize
    )

    override suspend fun loadLastFrame(animationId: UUID): Frame? =
        localDaoService.loadLastFrame(animationId)

    override suspend fun loadFirstFrame(animationId: UUID): Frame? =
        localDaoService.loadFirstFrame(animationId)

    override suspend fun loadNext(nextId: UUID): Frame? =
        localDaoService.loadNext(nextId)

    override suspend fun loadPrev(prevId: UUID): Frame? =
        localDaoService.loadPrev(prevId)

    override suspend fun loadById(id: UUID): Frame? =
        localDaoService.loadById(id)

    override suspend fun loadById(animationId: UUID, id: UUID): Frame? =
        localDaoService.loadById(animationId, id)

    override suspend fun addFrame(frame: Frame) {
        localDaoService.addFrame(frame)
    }

    override suspend fun removeFrame(frame: Frame) {
        localDaoService.removeFrame(frame)
    }

    override suspend fun removeByAnimation(animationId: UUID) {
        localDaoService.removeByAnimation(animationId)
    }
}