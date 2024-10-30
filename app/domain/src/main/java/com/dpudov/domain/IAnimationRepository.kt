package com.dpudov.domain

import kotlinx.coroutines.flow.Flow

interface IAnimationRepository {
    fun getLatestAnimation(): Flow<Animation?>

    suspend fun addAnimation(animation: Animation)
}