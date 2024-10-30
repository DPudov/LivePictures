package com.dpudov.frames.datasource.local.service

import IFrameDaoService
import com.dpudov.domain.Frame
import com.dpudov.frames.datasource.local.dao.FrameDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.FrameEntity
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class FrameDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IFrameDaoService {
    private val frameDao: FrameDao = appDatabase.frameDao()

    override suspend fun loadNextFrames(
        animationId: UUID,
        lastFrameId: UUID?,
        pageSize: Int
    ): List<Frame> =
        withContext(dispatcher) {
            frameDao.loadNextFrames(
                animationId = animationId,
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
            animationId = animationId,
            firstFrameId = firstFrameId,
            pageSize = pageSize
        ).map(FrameEntity::toData)
    }

    override suspend fun addFrame(frame: Frame) {
        withContext(dispatcher) {
            frameDao.addFrame(frame.toEntity())
        }
    }

    override suspend fun removeFrame(frameId: UUID) {
        withContext(dispatcher) {
            frameDao.removeFrame(frameId)
        }
    }
}