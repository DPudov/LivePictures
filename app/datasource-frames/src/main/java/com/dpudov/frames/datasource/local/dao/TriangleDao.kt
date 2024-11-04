package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.TriangleEntity
import java.util.UUID

@Dao
interface TriangleDao {
    @Query("select * from triangles where frameId = :frameId")
    suspend fun getByFrameId(frameId: UUID): List<TriangleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: TriangleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(entities: List<TriangleEntity>)

    @Query("delete from triangles where id = :id")
    suspend fun removeById(id: UUID)
}