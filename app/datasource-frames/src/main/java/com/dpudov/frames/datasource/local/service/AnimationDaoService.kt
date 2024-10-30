package com.dpudov.frames.datasource.local.service

import IAnimationDaoService
import com.dpudov.domain.Animation
import com.dpudov.frames.datasource.local.dao.AnimationDao
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.mapper.toData
import com.dpudov.frames.datasource.local.mapper.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnimationDaoService(
    appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IAnimationDaoService {
    private val animationDao: AnimationDao = appDatabase.animationDao()
    override fun getLatestAnimation(): Flow<Animation?> =
        animationDao.getLatestAnimation().map { entity ->
            entity?.toData()
        }.flowOn(dispatcher)

    override suspend fun addAnimation(animation: Animation) {
        withContext(dispatcher) {
            animationDao.addAnimation(animation.toEntity())
        }
    }
}
