@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass.sections.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * [StandardTestDispatcher] by default will not enter launch coroutine except we explicitly instruct
 * it to enter launch coroutine
 *
 * But using [UnconfinedTestDispatcher] will by default jump into the launch coroutine and there will be no need
 * calling [runCurrent] method to enter into the launch coroutine in our test case
 *
 * When should you use which, using [StandardTestDispatcher] when you want to have fine control over the
 * when to enter a launch block (maybe when you have nested launch block, and use use [UnconfinedTestDispatcher]
 * when you have the just have a launch block and it might save you from calling [runCurrent] method
 */
class MainCoroutineRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}