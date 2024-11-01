package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.PointEntity
import java.util.UUID

@Dao
interface PointDao {
    @Query("select * from points where strokeId = :strokeId")
    suspend fun getPointsByStrokeId(strokeId: UUID): List<PointEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(strokes: List<PointEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stroke: PointEntity)
}