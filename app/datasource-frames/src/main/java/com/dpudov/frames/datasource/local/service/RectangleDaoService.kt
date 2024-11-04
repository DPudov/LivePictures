package com.dpudov.frames.datasource.local.service

import com.dpudov.data.IRectangleDaoService
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Rect
import com.dpudov.frames.datasource.local.dao.RectangleDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.RectangleEntity
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class RectangleDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRectangleDaoService {
    private val rectangleDao: RectangleDao = appDatabase.rectangleDao()
    override suspend fun getByFrame(frameId: UUID): List<DrawableItem> =
        withContext(dispatcher) {
            rectangleDao.getByFrameId(frameId).map(RectangleEntity::toData)
        }

    override suspend fun add(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Rect) {
                rectangleDao.add(item.toEntity())
            }
        }
    }

    override suspend fun addAll(items: List<DrawableItem>) {
        withContext(dispatcher) {
            rectangleDao.addAll(
                items
                    .filterIsInstance<Rect>()
                    .map(Rect::toEntity)
            )
        }
    }

    override suspend fun remove(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Rect) {
                rectangleDao.removeById(item.id)
            }
        }
    }
}