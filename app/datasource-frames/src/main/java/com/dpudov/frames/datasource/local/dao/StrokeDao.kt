package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.StrokeEntity
import java.util.UUID

@Dao
interface StrokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStroke(stroke: StrokeEntity)

    @Query("delete from strokes where id = :strokeId")
    suspend fun removeStroke(strokeId: UUID)
}