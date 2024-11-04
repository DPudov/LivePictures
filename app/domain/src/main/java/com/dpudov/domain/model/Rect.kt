package com.dpudov.domain.model

import java.util.UUID

data class Rect(
    override val id: UUID,
    override val frameId: UUID,
    override val finishTimestamp: Long,
    override val color: Int,
    override val thickness: Float,
    val topLeftX: Float,
    val topLeftY: Float,
    val width: Float,
    val height: Float
) : DrawableItem
