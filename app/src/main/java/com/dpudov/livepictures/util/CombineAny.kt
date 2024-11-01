package com.dpudov.livepictures.util

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlin.experimental.ExperimentalTypeInference

fun <T> Flow<T>.prependNull(): Flow<T?> = flow {
    emit(null)
    emitAll(this@prependNull)
}

@JvmName("flowCombineAny")
fun <T1, T2, R> Flow<T1>.combineAny(
    flow: Flow<T2>,
    transform: suspend (a: T1?, b: T2?) -> R
): Flow<R> =
    prependNull().combine(flow.prependNull(), transform)

@OptIn(ExperimentalTypeInference::class)
inline fun <reified T, R> combineAny(
    vararg flows: Flow<T>,
    @BuilderInference crossinline transform: suspend (Array<T?>) -> R
): Flow<R> =
    combine(flows.map { it.prependNull() }, transform)

fun <T1, T2, R> combineAny(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    transform: suspend (a: T1?, b: T2?) -> R
): Flow<R> = combine(
    flow.prependNull(),
    flow2.prependNull(),
    transform
)

fun <T1, T2, T3, R> combineAny(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    transform: suspend (a: T1?, b: T2?, c: T3?) -> R
): Flow<R> = combine(
    flow.prependNull(),
    flow2.prependNull(),
    flow3.prependNull(),
    transform
)

suspend fun <T, R> Iterable<T>.parallelMap(block: suspend (T) -> R): List<R> = coroutineScope {
    map { async { block(it) } }.awaitAll()
}