package com.dpudov.livepictures.di

import com.dpudov.data.AnimationRepository
import com.dpudov.data.DrawableItemRepository
import com.dpudov.data.FrameRepository
import com.dpudov.data.IAnimationDaoService
import com.dpudov.data.ICircleDaoService
import com.dpudov.data.IFrameDaoService
import com.dpudov.data.IInstrumentDaoService
import com.dpudov.data.IStrokeDaoService
import com.dpudov.data.ITriangleDaoService
import com.dpudov.data.InstrumentRepository
import com.dpudov.domain.repository.IAnimationRepository
import com.dpudov.domain.repository.IDrawableItemRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.repository.IInstrumentRepository
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
    fun provideInstrumentRepository(
        localDaoService: IInstrumentDaoService
    ): IInstrumentRepository = InstrumentRepository(
        localDaoService = localDaoService
    )

    @Provides
    fun provideDrawableItemRepository(
        localStrokeDaoService: IStrokeDaoService,
        localCircleDaoService: ICircleDaoService,
        localTriangleDaoService: ITriangleDaoService
    ): IDrawableItemRepository = DrawableItemRepository(
        localStrokeDaoService = localStrokeDaoService,
        localCircleDaoService = localCircleDaoService,
        localTriangleDaoService = localTriangleDaoService
    )
}