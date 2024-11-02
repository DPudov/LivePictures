package com.dpudov.domain.model

import java.util.UUID

data class Animation(
    val id: UUID,
    val name: String,
    val createdAt: Long,
    val fps: Int
) {
    companion object {
        const val DEFAULT_FPS = 1
    }
}