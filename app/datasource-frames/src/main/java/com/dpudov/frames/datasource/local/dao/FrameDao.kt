package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.FrameEntity
import java.util.UUID

@Dao
interface FrameDao {
    @Query("select * from frames where animationId = :animationId and (prevFrameId = :lastFrameId OR :lastFrameId IS NULL) LIMIT :pageSize")
    suspend fun loadNextFrames(
        animationId: UUID,
        lastFrameId: UUID?,
        pageSize: Int
    ): List<FrameEntity>

    @Query("select * from frames WHERE animationId = :animationId AND (nextFrameId = :firstFrameId OR :firstFrameId IS NULL) LIMIT :pageSize")
    suspend fun loadPreviousFrames(
        animationId: UUID,
        firstFrameId: UUID?,
        pageSize: Int
    ): List<FrameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFrame(frame: FrameEntity)

    @Query("delete from frames where id = :frameId")
    suspend fun removeFrame(frameId: UUID)

    @Query("update frames set nextFrameId = :newNextId WHERE id = :prevFrameId")
    suspend fun updateNextIdOnPrev(prevFrameId: UUID, newNextId: UUID?)

    @Query("update frames set prevFrameId = :newPrevId WHERE id = :nextFrameId")
    suspend fun updatePrevIdOnNext(nextFrameId: UUID, newPrevId: UUID?)
}