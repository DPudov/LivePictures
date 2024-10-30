package com.dpudov.livepictures.di

import com.dpudov.data.AnimationRepository
import com.dpudov.data.FrameRepository
import com.dpudov.data.IAnimationDaoService
import com.dpudov.data.IFrameDaoService
import com.dpudov.data.IStrokeDaoService
import com.dpudov.data.StrokeRepository
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