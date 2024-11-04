package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.RectangleEntity
import java.util.UUID

@Dao
interface RectangleDao {
    @Query("select * from rectangles where frameId = :frameId")
    suspend fun getByFrameId(frameId: UUID): List<RectangleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: RectangleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(entities: List<RectangleEntity>)

    @Query("delete from rectangles where id = :id")
    suspend fun removeById(id: UUID)
}