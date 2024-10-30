package com.dpudov.frames.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "figures")
data class FigureEntity(
    @PrimaryKey
    val id: UUID,

    val name: String
)
