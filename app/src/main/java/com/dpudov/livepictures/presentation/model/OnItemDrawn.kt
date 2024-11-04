package com.dpudov.livepictures.presentation.model

import com.dpudov.domain.model.DrawableItem

fun interface OnItemDrawn {
    fun onItemDrawn(item: DrawableItem)
}