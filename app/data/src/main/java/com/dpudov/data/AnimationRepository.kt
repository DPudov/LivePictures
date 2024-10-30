package com.dpudov.data

import com.dpudov.domain.model.Animation
import com.dpudov.domain.repository.IAnimationRepository
import kotlinx.coroutines.flow.Flow

class AnimationRepository(
    private val localDaoService: IAnimationDaoService
): IAnimationRepository {
    override fun getLatestAnimation(): Flow<Animation?> = localDaoService.getLatestAnimation()

    override suspend fun addAnimation(animation: Animation) {
        localDaoService.addAnimation(animation)
    }
}