package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "triangles",
    foreignKeys = [
        ForeignKey(
            entity = FrameEntity::class,
            parentColumns = ["id"],
            childColumns = ["frameId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["frameId"])
    ]
)
data class TriangleEntity(
    @PrimaryKey
    val id: UUID,
    val frameId: UUID,
    val finishTimestamp: Long,
    val color: Int,
    val thickness: Float,
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val x3: Float,
    val y3: Float
)
