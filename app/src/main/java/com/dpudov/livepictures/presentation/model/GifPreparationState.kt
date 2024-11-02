package com.dpudov.livepictures.presentation.model

sealed class GifPreparationState {
    data object Idle : GifPreparationState()

    data object Loading : GifPreparationState()
}