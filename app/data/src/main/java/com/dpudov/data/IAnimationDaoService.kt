package com.dpudov.data

import com.dpudov.domain.model.Animation
import kotlinx.coroutines.flow.Flow

interface IAnimationDaoService {
    fun getLatestAnimation(): Flow<Animation?>

    suspend fun updateAnimation(animation: Animation)

    suspend fun addAnimation(animation: Animation)
}