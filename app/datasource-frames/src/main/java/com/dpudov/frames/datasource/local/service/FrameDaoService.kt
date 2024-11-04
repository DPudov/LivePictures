package com.dpudov.frames.datasource.local.service

import com.dpudov.data.IFrameDaoService
import com.dpudov.domain.model.Frame
import com.dpudov.frames.datasource.local.dao.FrameDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.FrameEntity
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class FrameDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFrameDaoService {
    private val frameDao: FrameDao = appDatabase.frameDao()
    override fun loadAnyByIds(ids: List<UUID>): Flow<List<Frame>> = frameDao.loadAnyByIds(ids)
        .map {
            it.map(FrameEntity::toData)
        }
        .flowOn(dispatcher)

    override suspend fun loadNextFrames(
        animationId: UUID,
        lastFrameId: UUID?,
        pageSize: Int
    ): List<Frame> =
        withContext(dispatcher) {
            frameDao.loadNextFrames(
                lastFrameId = lastFrameId,
                pageSize = pageSize
            ).map(FrameEntity::toData)
        }


    override suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<Frame> = withContext(dispatcher) {
        frameDao.loadPreviousFrames(
            firstFrameId = firstFrameId,
            pageSize = pageSize
        ).map(FrameEntity::toData)
    }

    override suspend fun loadLastFrame(animationId: UUID): Frame? = withContext(dispatcher) {
        frameDao.loadLastFrame(animationId)?.toData()
    }

    override suspend fun loadFirstFrame(animationId: UUID): Frame? = withContext(dispatcher) {
        frameDao.loadFirstFrame(animationId)?.toData()
    }

    override suspend fun loadById(id: UUID): Frame? = withContext(dispatcher) {
        frameDao.loadById(id)?.toData()
    }

    override suspend fun loadById(animationId: UUID, id: UUID): Frame? = withContext(dispatcher) {
        frameDao.loadById(
            animationId = animationId,
            id = id
        )?.toData()
    }

    override suspend fun addFrame(frame: Frame) {
        withContext(dispatcher) {
            val prevId = frame.prevId
            val nextId = frame.nextId
            if (prevId != null) {
                frameDao.updateNextIdOnPrev(prevFrameId = prevId, newNextId = frame.id)
            }
            if (nextId != null) {
                frameDao.updatePrevIdOnNext(nextFrameId = nextId, newPrevId = frame.id)
            }
            frameDao.addFrame(frame.toEntity())
        }
    }

    override suspend fun removeFrame(frame: Frame) {
        withContext(dispatcher) {
            val prevId = frame.prevId
            val nextId = frame.nextId
            if (prevId != null) {
                frameDao.updateNextIdOnPrev(prevFrameId = prevId, newNextId = nextId)
            }
            if (nextId != null) {
                frameDao.updatePrevIdOnNext(nextFrameId = nextId, newPrevId = prevId)
            }
            frameDao.removeFrame(frameId = frame.id)
        }
    }

    override suspend fun removeByAnimation(animationId: UUID) {
        withContext(dispatcher) {
            frameDao.removeByAnimationId(animationId)
        }
    }
}