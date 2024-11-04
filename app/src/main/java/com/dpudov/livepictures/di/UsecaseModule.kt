package com.dpudov.livepictures.di

import com.dpudov.domain.repository.IDrawableItemRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.usecase.GenerateFramesUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UsecaseModule {
    @Provides
    fun provideGenerateFramesUsecase(
        frameRepository: IFrameRepository,
        drawableItemRepository: IDrawableItemRepository
    ): GenerateFramesUsecase = GenerateFramesUsecase(
        frameRepository = frameRepository,
        drawableItemRepository = drawableItemRepository
    )
}