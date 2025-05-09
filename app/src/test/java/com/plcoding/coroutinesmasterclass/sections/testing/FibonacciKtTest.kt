@file:OptIn(ExperimentalStdlibApi::class)

package com.plcoding.coroutinesmasterclass.sections.testing

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class FibonacciKtTest {

    @Test
    fun testFibonacci() = runTest {
        val dispatcher = coroutineContext[CoroutineDispatcher]
        val result = fib(30, dispatcher!!)

        assertTrue(result == 832_040)
    }
}