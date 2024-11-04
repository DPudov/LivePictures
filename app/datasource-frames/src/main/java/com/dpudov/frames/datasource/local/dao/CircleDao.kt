package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.CircleEntity
import java.util.UUID

@Dao
interface CircleDao {
    @Query("select * from circles where frameId = :frameId")
    suspend fun getByFrameId(frameId: UUID): List<CircleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: CircleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(entities: List<CircleEntity>)

    @Query("delete from circles where id = :id")
    suspend fun removeById(id: UUID)
}