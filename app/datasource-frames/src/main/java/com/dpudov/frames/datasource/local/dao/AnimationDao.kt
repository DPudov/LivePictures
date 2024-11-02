package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dpudov.frames.datasource.local.entity.AnimationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimationDao {
    @Query("select * from animations order by createdAt desc limit 1")
    fun getLatestAnimation(): Flow<AnimationEntity?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(animation: AnimationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAnimation(animation: AnimationEntity)
}