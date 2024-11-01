package com.dpudov.domain.model

import java.util.UUID

data class Point(
    val id: Long,
    val strokeId: UUID,
    val x: Float,
    val y: Float
)
