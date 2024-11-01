package com.dpudov.livepictures.presentation.model

sealed class AnimationState {
    data object Running: AnimationState()

    data object Idle: AnimationState()
}