package com.dpudov.livepictures

sealed class ButtonState {
    data object Active : ButtonState()

    data object Inactive : ButtonState()
}