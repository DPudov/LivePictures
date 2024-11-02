package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "frames",
    foreignKeys = [
        ForeignKey(
            entity = AnimationEntity::class,
            parentColumns = ["id"],
            childColumns = ["animationId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["animationId"])
    ]
)
data class FrameEntity(
    @PrimaryKey
    val id: UUID,

    val animationId: UUID,

    val prevFrameId: UUID?,

    val nextFrameId: UUID?
)
