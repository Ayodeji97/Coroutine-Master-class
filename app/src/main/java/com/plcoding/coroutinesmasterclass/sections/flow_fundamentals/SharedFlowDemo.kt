package com.plcoding.coroutinesmasterclass.sections.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * A sharedFlow can emit values even when there is no active collector. Unlike cold flow, that need
 * to be launched in a coroutine to start emitting values, sharedFlow is hot and can emit values without
 * a collector.
 *
 * 1. The replay parameter allows you to specify how many values can be buffered before an active
 * collector, and when there is a collector, the flow will emit those buffered values.
 *
 * 2. The flow emission will suspend until all collectors has processed this values
 * 3. The extraBufferCapacity parameter allows you to specify how many last values can be cached/ buffer for the slower collector
 * before the flow starts dropping values. And this is useful when you have multiple collectors and some of them are slow.
 * As the fast collector will not be blocked by the slow collector.
 * 4. The onBufferOverflow parameter allows you to specify what to do when the buffer is full.
 */
fun sharedFlowDemo() {
    val sharedFlow = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 3, // This is needed when there are multiple collectors but some are slow
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    GlobalScope.launch {
        sharedFlow.onEach {
            println("Collector 1: $it")
            delay(5000L)
        }.launchIn(GlobalScope)

        sharedFlow.onEach {
            println("Collector 2: $it")
        }.launchIn(GlobalScope)
    }

    GlobalScope.launch {
        repeat(10) {
            delay(500L)
            sharedFlow.emit(it)
        }
    }
}