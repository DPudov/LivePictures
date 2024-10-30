package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "figures")
data class FigureEntity(
    val id: UUID,

    val name: String
)
