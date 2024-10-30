package com.dpudov.livepictures.presentation.model

sealed class ButtonState {
    data object Active : ButtonState()

    data object Inactive : ButtonState()
}