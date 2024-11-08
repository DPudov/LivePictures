package com.dpudov.frames.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dpudov.frames.datasource.local.dao.AnimationDao
import com.dpudov.frames.datasource.local.dao.CircleDao
import com.dpudov.frames.datasource.local.dao.FrameDao
import com.dpudov.frames.datasource.local.dao.InstrumentDao
import com.dpudov.frames.datasource.local.dao.PointDao
import com.dpudov.frames.datasource.local.dao.RectangleDao
import com.dpudov.frames.datasource.local.dao.StrokeDao
import com.dpudov.frames.datasource.local.dao.TriangleDao
import com.dpudov.frames.datasource.local.entity.AnimationEntity
import com.dpudov.frames.datasource.local.entity.CircleEntity
import com.dpudov.frames.datasource.local.entity.FrameEntity
import com.dpudov.frames.datasource.local.entity.InstrumentEntity
import com.dpudov.frames.datasource.local.entity.PointEntity
import com.dpudov.frames.datasource.local.entity.RectangleEntity
import com.dpudov.frames.datasource.local.entity.StrokeEntity
import com.dpudov.frames.datasource.local.entity.TriangleEntity

@Database(
    entities = [
        AnimationEntity::class,
        FrameEntity::class,
        StrokeEntity::class,
        CircleEntity::class,
        TriangleEntity::class,
        RectangleEntity::class,
        InstrumentEntity::class,
        PointEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animationDao(): AnimationDao
    abstract fun frameDao(): FrameDao
    abstract fun strokeDao(): StrokeDao
    abstract fun pointDao(): PointDao
    abstract fun instrumentDao(): InstrumentDao
    abstract fun circleDao(): CircleDao
    abstract fun triangleDao(): TriangleDao
    abstract fun rectangleDao(): RectangleDao

    companion object {
        const val DATABASE_NAME = "live-pictures-frames"
    }
}