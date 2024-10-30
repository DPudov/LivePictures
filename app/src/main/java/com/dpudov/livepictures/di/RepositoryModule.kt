package com.dpudov.livepictures.di

import AnimationRepository
import FrameRepository
import IAnimationDaoService
import IFrameDaoService
import IStrokeDaoService
import StrokeRepository
import com.dpudov.domain.repository.IAnimationRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.repository.IStrokeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideAnimationRepository(
        localDaoService: IAnimationDaoService
    ): IAnimationRepository = AnimationRepository(
        localDaoService = localDaoService
    )

    @Provides
    fun provideFrameRepository(
        localDaoService: IFrameDaoService
    ): IFrameRepository = FrameRepository(
        localDaoService = localDaoService
    )

    @Provides
    fun provideStrokeRepository(
        localDaoService: IStrokeDaoService
    ): IStrokeRepository = StrokeRepository(
        localDaoService = localDaoService
    )
}