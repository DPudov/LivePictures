package com.dpudov.frames.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dpudov.frames.datasource.local.entity.AnimationEntity
import com.dpudov.frames.datasource.local.entity.FigureEntity
import com.dpudov.frames.datasource.local.entity.FrameEntity
import com.dpudov.frames.datasource.local.entity.InstrumentEntity
import com.dpudov.frames.datasource.local.entity.StrokeEntity

@Database(
    entities = [
        AnimationEntity::class,
        FrameEntity::class,
        StrokeEntity::class,
        FigureEntity::class,
        InstrumentEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "live-pictures-frames"
    }
}