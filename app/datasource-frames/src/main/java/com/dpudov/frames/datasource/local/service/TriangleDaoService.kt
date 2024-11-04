package com.dpudov.frames.datasource.local.service

import com.dpudov.data.ITriangleDaoService
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Triangle
import com.dpudov.frames.datasource.local.dao.TriangleDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.entity.TriangleEntity
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class TriangleDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ITriangleDaoService {
    private val triangleDao: TriangleDao = appDatabase.triangleDao()
    override suspend fun getByFrame(frameId: UUID): List<DrawableItem> =
        withContext(dispatcher) {
            triangleDao.getByFrameId(frameId).map(TriangleEntity::toData)
        }


    override suspend fun add(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Triangle) {
                triangleDao.add(item.toEntity())
            }
        }
    }

    override suspend fun addAll(items: List<DrawableItem>) {
        withContext(dispatcher) {
            triangleDao.addAll(
                items
                    .filterIsInstance<Triangle>()
                    .map(Triangle::toEntity)
            )
        }
    }

    override suspend fun remove(item: DrawableItem) {
        withContext(dispatcher) {
            if (item is Triangle) {
                triangleDao.removeById(item.id)
            }
        }
    }
}