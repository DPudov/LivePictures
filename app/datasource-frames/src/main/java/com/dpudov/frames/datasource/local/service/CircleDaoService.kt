package com.dpudov.frames.datasource.local.service

import com.dpudov.data.ICircleDaoService
import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.frames.datasource.local.dao.CircleDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.CircleEntity
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class CircleDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ICircleDaoService {
    private val circleDao: CircleDao = appDatabase.circleDao()
    override suspend fun getByFrame(frameId: UUID): List<Circle> =
        withContext(dispatcher) {
            circleDao.getByFrameId(frameId).map(CircleEntity::toData)
        }


    override suspend fun add(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Circle) {
                circleDao.add(item.toEntity())
            }
        }
    }

    override suspend fun addAll(items: List<DrawableItem>) {
        withContext(dispatcher) {
            circleDao.addAll(
                items
                    .filterIsInstance<Circle>()
                    .map(Circle::toEntity)
            )
        }
    }

    override suspend fun remove(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Circle) {
                circleDao.removeById(item.id)
            }
        }
    }
}