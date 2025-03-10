package com.plcoding.coroutinesmasterclass.sections.testing

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val immediate: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

object StandardDispatcher : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}