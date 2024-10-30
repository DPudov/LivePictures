package com.dpudov.livepictures.presentation.model

sealed class SelectedState {
    data object Selected : SelectedState()

    data object Idle : SelectedState()
}
