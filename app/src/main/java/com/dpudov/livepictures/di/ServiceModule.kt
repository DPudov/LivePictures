package com.dpudov.livepictures.di

import com.dpudov.data.IAnimationDaoService
import com.dpudov.data.IFrameDaoService
import com.dpudov.data.IInstrumentDaoService
import com.dpudov.data.IStrokeDaoService
import com.dpudov.frames.datasource.local.database.AppDatabase
import com.dpudov.frames.datasource.local.service.AnimationDaoService
import com.dpudov.frames.datasource.local.service.FrameDaoService
import com.dpudov.frames.datasource.local.service.InstrumentDaoService
import com.dpudov.frames.datasource.local.service.StrokeDaoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {
    @Provides
    fun provideAnimationDaoService(
        appDatabase: AppDatabase
    ): IAnimationDaoService =
        AnimationDaoService(appDatabase = appDatabase)

    @Provides
    fun provideFrameDaoService(
        appDatabase: AppDatabase
    ): IFrameDaoService =
        FrameDaoService(appDatabase = appDatabase)

    @Provides
    fun provideStrokeDaoService(
        appDatabase: AppDatabase
    ): IStrokeDaoService =
        StrokeDaoService(appDatabase = appDatabase)

    @Provides
    fun provideInstrumentDaoService(
        appDatabase: AppDatabase
    ): IInstrumentDaoService =
        InstrumentDaoService(appDatabase = appDatabase)
}