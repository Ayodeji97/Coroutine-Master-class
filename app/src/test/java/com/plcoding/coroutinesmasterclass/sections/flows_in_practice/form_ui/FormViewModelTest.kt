@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.form_ui


import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.plcoding.coroutinesmasterclass.sections.testing.MainCoroutineRule
import com.plcoding.coroutinesmasterclass.sections.testing.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FormViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var formViewModel: FormViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers(testDispatcher = mainCoroutineRule.testDispatcher)
        formViewModel = FormViewModel(
            dispatcher = testDispatchers
        )
    }

    @Test
    fun testRegisterLoadingWithCoroutine() = runTest(testDispatchers.testDispatcher) {
        assertThat(formViewModel.isLoading.value).isFalse()

        formViewModel.register()

        runCurrent() // This is needed for entering any launch coroutine

        assertThat(formViewModel.isLoading.value).isTrue()

        advanceUntilIdle() // this is needed for when we hit a suspension point inside of te launch coroutine
        /**
         * This is needed if we even want more fine grained - for instance where we have multiple suspension point
         * or task within a launch coroutine, we don't want to advance until the end (advanceUntilIdle) but rather
         * set by step
         */
        //advanceTimeBy(3050L)
        assertThat(formViewModel.isLoading.value).isFalse()
    }

    @Test
    fun testRegisterLoadingWithFlow() = runTest(testDispatchers.testDispatcher) {
        formViewModel.isLoading.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isFalse()

            formViewModel.register()

            val loadingEmission = awaitItem()
            assertThat(loadingEmission).isTrue()

            val notLoadingEmission = awaitItem()
            assertThat(notLoadingEmission).isFalse()
        }
    }

    @Test
    fun canRegister() = runTest(testDispatchers.testDispatcher) {
        formViewModel.canRegister.test {

            formViewModel.onEmailChange("test@test.com")
            formViewModel.onPasswordChange("test23412")

            assertThat(awaitItem()).isFalse()
            formViewModel.onPasswordChange("test23412*")
            assertThat(awaitItem()).isTrue()
        }
    }

}