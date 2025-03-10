package com.plcoding.coroutinesmasterclass.sections.testing

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PrimeNumberAssignmentTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var primeNumberAssignment: PrimeNumberAssignment

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers(testDispatcher = mainCoroutineRule.testDispatcher)
        primeNumberAssignment = PrimeNumberAssignment(testDispatchers)
    }

    @Test
    fun testPrimeNumberAtIndex() = runTest {
        val result = findPrimeNumberAtIndex(10)
        assertTrue(result == 29)
    }
}