package com.dpudov.frames.datasource.local.service

import com.dpudov.data.IStrokeDaoService
import com.dpudov.domain.model.Point
import com.dpudov.domain.model.Stroke
import com.dpudov.frames.datasource.local.dao.PointDao
import com.dpudov.frames.datasource.local.dao.StrokeDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.StrokeWithPoints
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class StrokeDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IStrokeDaoService {
    private val strokeDao: StrokeDao = appDatabase.strokeDao()
    private val pointDao: PointDao = appDatabase.pointDao()
    override suspend fun getStrokesByFrame(frameId: UUID) =
        withContext(dispatcher) {
            strokeDao.getFullByFrameId(frameId)
                .map(StrokeWithPoints::toData)
                .sortedBy(Stroke::finishTimestamp)
        }


    override suspend fun addStroke(stroke: Stroke) {
        withContext(dispatcher) {
            strokeDao.addStroke(stroke.toEntity())
            pointDao.insertAll(stroke.points.map(Point::toEntity))
        }
    }

    override suspend fun removeStroke(strokeId: UUID) {
        withContext(dispatcher) {
            strokeDao.removeStroke(strokeId)
        }
    }

}