package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.backpressure

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * We can hanlde backpressure in flows using the following operators:
 * 1. Buffer - This will buffer the emissions and allow the flow to continue emitting while the collector is busy
 * 2. Conflate - This will skip the emissions and only emit the latest one
 * 3. CollectLatest - This will cancel the previous emission and only emit the latest one
 *
 * When to use what
 * 1. Buffer - When you want to buffer the emissions and allow the flow to continue emitting while the collector is busy,
 * use if you care about all emissions
 * 2. Conflate - When you want to skip the emissions and only emit the latest one, use if you don't care about the order of emissions
 * 3. CollectLatest - When you want to cancel the previous emission and only emit the latest one, use if you care about the latest emission
 */
fun backpressureDemo() {
    GlobalScope.launch {
        flow {
            println("FOOD: Preparing appetizer")
            delay(500L)
            emit("Appetizer")

            println("FOOD: Preparing main dish")
            delay(1000L)
            emit("Main dish")

            println("FOOD: Preparing dessert")
            delay(500L)
            emit("Dessert")
        }.collectLatest { dish ->
            println("FOOD: Start eating $dish")
            delay(2500L)
            println("FOOD: Finished eating $dish")
        }
    }
}