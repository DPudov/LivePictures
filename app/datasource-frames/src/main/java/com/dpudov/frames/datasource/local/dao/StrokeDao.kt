package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dpudov.frames.datasource.local.entity.StrokeEntity
import com.dpudov.frames.datasource.local.entity.StrokeWithPoints
import java.util.UUID

@Dao
interface StrokeDao {
    @Transaction
    @Query("select * from strokes where frameId = :frameId")
    suspend fun getFullByFrameId(frameId: UUID): List<StrokeWithPoints>

    @Query("select * from strokes where frameId = :frameId")
    suspend fun getByFrameId(frameId: UUID): List<StrokeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStroke(stroke: StrokeEntity)

    @Query("delete from strokes where id = :strokeId")
    suspend fun removeStroke(strokeId: UUID)
}