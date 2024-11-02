package com.dpudov.domain.repository

import com.dpudov.domain.model.Animation
import kotlinx.coroutines.flow.Flow

interface IAnimationRepository {
    fun getLatestAnimation(): Flow<Animation?>

    suspend fun updateAnimation(animation: Animation)

    suspend fun addAnimation(animation: Animation)
}