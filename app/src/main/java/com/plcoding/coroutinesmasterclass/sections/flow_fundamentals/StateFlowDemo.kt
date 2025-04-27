package com.plcoding.coroutinesmasterclass.sections.flow_fundamentals

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * StateFlow is a state holder observable flow that emits its current and new state updates.
 * Unlike SharedFlow and Flow, StateFlow is always active and has a current value. That is, it will
 * the initial value can be set when creating the StateFlow object, and it will always emit the initial value
 *
 * Unlike Flow, StateFlow will emit the last value to the new collector, but normal flow
 * will emit the whole block of code to the new collector.
 */
fun stateFlowDemo() {
    val stateFlow = MutableStateFlow(0)

    stateFlow.onEach {
        println("Value is $it")
    }.launchIn(GlobalScope)

    GlobalScope.launch {
        repeat(10) {
            delay(500L)
            stateFlow.value = it
        }
        stateFlow.onEach {
            println("Value from collector 2 is $it")
        }.launchIn(GlobalScope)
    }
}