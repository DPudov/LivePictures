package com.dpudov.livepictures.presentation.model

sealed class GenerationState {
    data class Generating(
        val number: Int,
        val total: Int
    ) : GenerationState()

    data object Idle : GenerationState()
}