package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "rectangles",
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
data class RectangleEntity(
    @PrimaryKey
    val id: UUID,
    val frameId: UUID,
    val finishTimestamp: Long,
    val color: Int,
    val thickness: Float,
    val topLeftX: Float,
    val topLeftY: Float,
    val width: Float,
    val height: Float
)