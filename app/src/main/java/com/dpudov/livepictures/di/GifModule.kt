package com.dpudov.livepictures.di

import com.dpudov.exporter.exporter.GifExporter
import com.dpudov.exporter.exporter.IGifExporter
import com.dpudov.exporter.repository.GifRepository
import com.dpudov.exporter.repository.IGifExportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GifModule {
    @Provides
    fun provideGifExporter(): IGifExporter = GifExporter()

    @Provides
    fun provideGifRepository(
        gifExporter: IGifExporter
    ): IGifExportRepository = GifRepository(gifExporter = gifExporter)
}