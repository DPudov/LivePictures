package com.dpudov.frames.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpudov.frames.datasource.local.entity.FrameEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface FrameDao {
    @Query("select * from frames where id in (:ids)")
    fun loadAnyByIds(ids: List<UUID>): Flow<List<FrameEntity>>

    @Query("""
        WITH RECURSIVE NextFrames AS (
            SELECT *
            FROM frames
            WHERE id = :lastFrameId

            UNION ALL

            SELECT fe.id, fe.animationId, fe.prevFrameId, fe.nextFrameId
            FROM frames fe
            INNER JOIN NextFrames nf ON fe.id = nf.nextFrameId
            WHERE nf.nextFrameId IS NOT NULL
            LIMIT :pageSize
        )
        SELECT *
        FROM NextFrames
        WHERE id != :lastFrameId
    """)
    suspend fun loadNextFrames(
        lastFrameId: UUID?,
        pageSize: Int
    ): List<FrameEntity>

    @Query(" WITH RECURSIVE PreviousFrames AS (\n" +
            "            SELECT *\n" +
            "            FROM frames\n" +
            "            WHERE id = :firstFrameId\n" +
            "\n" +
            "            UNION ALL\n" +
            "\n" +
            "            SELECT fe.id, fe.animationId, fe.prevFrameId, fe.nextFrameId\n" +
            "            FROM frames fe\n" +
            "            INNER JOIN PreviousFrames pf ON fe.id = pf.prevFrameId\n" +
            "            WHERE pf.prevFrameId IS NOT NULL\n" +
            "            LIMIT :pageSize\n" +
            "        )\n" +
            "        SELECT *\n" +
            "        FROM PreviousFrames\n" +
            "        WHERE id != :firstFrameId")
    suspend fun loadPreviousFrames(
        firstFrameId: UUID?,
        pageSize: Int
    ): List<FrameEntity>

    @Query("select * from frames where animationId = :animationId and nextFrameId is null")
    suspend fun loadLastFrame(animationId: UUID): FrameEntity?

    @Query("select * from frames where animationId = :animationId and prevFrameId is null")
    suspend fun loadFirstFrame(animationId: UUID): FrameEntity?

    @Query("select * from frames where id = :id")
    suspend fun loadById(id: UUID): FrameEntity?

    @Query("select * from frames where animationId = :animationId and id = :id")
    suspend fun loadById(animationId: UUID, id: UUID): FrameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFrame(frame: FrameEntity)

    @Query("delete from frames where id = :frameId")
    suspend fun removeFrame(frameId: UUID)

    @Query("delete from frames where animationId = :animationId")
    suspend fun removeByAnimationId(animationId: UUID)

    @Query("update frames set nextFrameId = :newNextId WHERE id = :prevFrameId")
    suspend fun updateNextIdOnPrev(prevFrameId: UUID, newNextId: UUID?)

    @Query("update frames set prevFrameId = :newPrevId WHERE id = :nextFrameId")
    suspend fun updatePrevIdOnNext(nextFrameId: UUID, newPrevId: UUID?)
}