@file:OptIn(ExperimentalStdlibApi::class)

package com.plcoding.coroutinesmasterclass.sections.testing

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class PrimeNumberAssignmentKtTest {

    @Test
    fun testPrimeNumberAtIndex() = runTest {
        val dispatcher = coroutineContext[CoroutineDispatcher]
        val result = findPrimeNumberAtIndex(10, dispatcher!!)
        assertTrue(result == 29)
    }
}