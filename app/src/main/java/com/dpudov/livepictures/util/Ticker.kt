package com.dpudov.livepictures.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun tickerFlow(period: Long, initialDelay: Long = 0) = flow {
    delay(initialDelay)
    while (currentCoroutineContext().isActive) {
        emit(Unit)
        delay(period)
    }
}
