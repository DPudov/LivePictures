package com.dpudov.domain.model

import java.util.UUID

sealed interface DrawableItem {
    val id: UUID
    val finishTimestamp: Long
    val color: Int
    val thickness: Float
}