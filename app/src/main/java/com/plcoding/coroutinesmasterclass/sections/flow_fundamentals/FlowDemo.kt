package com.plcoding.coroutinesmasterclass.sections.flow_fundamentals

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

fun flowDemo() {
    val flow = flow<Int> {
        println("Collection started!")
        delay(1000L)
        emit(1)
        delay(2000L)
        emit(2)
        delay(3000L)
        emit(3)
    }.shareIn(
        GlobalScope,
        SharingStarted.Eagerly,
    )

    flow.onEach {
        println("Collector 1: $it")
    }.launchIn(GlobalScope)

    GlobalScope.launch {
        delay(5000L)
        flow.onEach {
            println("Collector 2: $it")
        }.launchIn(GlobalScope)
    }

    val customScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    customScope.launch {
        flow.onEach {
            println("Collector 3: $it")
        }
    }

    flow.onEach {
        println("Collector 3: $it")
    }.launchIn(customScope)
}


fun myFlowDemo() {
    flow<Int> {
        println("Collection started Integer!")
        delay(1000L)
        emit(1)
        delay(2000L)
        emit(2)
        delay(3000L)
        emit(3)
    }.flatMapLatest {
       flow {
           println("Collection started String!")
           delay(1000L)
           emit("One!")
           delay(2000L)
           emit("Two!")
           delay(3000L)
           emit("Three!")
       }
    }.onEach {
        println("Collector 1: $it")
    }.launchIn(GlobalScope)
}