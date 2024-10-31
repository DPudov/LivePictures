package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "points")
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val strokeId: UUID,
    val x: Float,
    val y: Float
)
