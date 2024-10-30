package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "animations")
data class AnimationEntity(
    @PrimaryKey
    val id: UUID,

    val name: String
)
