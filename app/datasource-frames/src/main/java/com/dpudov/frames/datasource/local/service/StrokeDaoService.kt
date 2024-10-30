package com.dpudov.frames.datasource.local.service

import IStrokeDaoService
import com.dpudov.domain.model.Stroke
import com.dpudov.frames.datasource.local.dao.StrokeDao
import com.dpudov.frames.datasource.local.database.AppDatabase
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
    override suspend fun addStroke(stroke: Stroke) {
        withContext(dispatcher) {
            strokeDao.addStroke(stroke.toEntity())
        }
    }

    override suspend fun removeStroke(strokeId: UUID) {
        withContext(dispatcher) {
            strokeDao.removeStroke(strokeId)
        }
    }

}