@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass.sections.testing.search

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.plcoding.coroutinesmasterclass.sections.testing.MainCoroutineRule
import com.plcoding.coroutinesmasterclass.sections.testing.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration

class SearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers(testDispatcher = mainCoroutineRule.testDispatcher)
        searchViewModel = SearchViewModel()
    }

    @Test
    fun isInputValid_emptyInput_returnsFalse() = runTest {
        val input = ""
        val result = searchViewModel.isInputValid(input)
        assertThat(result).isFalse()
    }

    @Test
    fun isInputValid_validInput_returnsTrue() = runTest {
        val input = "Kotlin"
        val result = searchViewModel.isInputValid(input)
        assertThat(result).isTrue()
    }

    @Test
    fun isInputValid_invalidInput_returnsFalse() = runTest {
        val input = " "
        val result = searchViewModel.isInputValid(input)
        assertThat(result).isFalse()
    }

    @Test
    fun validationMessages_invalidInput_returnsCorrectInvalidMessage() = runTest {
        searchViewModel.validationMessages.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            searchViewModel.isInputValid("2")
            val canNotContainDigitEmission = awaitItem()
            assertThat(canNotContainDigitEmission).isEqualTo(listOf("Input cannot contain a digit"))
        }
    }

    @Test
    fun validationMessages_emptyInput_returnsCorrectEmptyMessage() = runTest {
        searchViewModel.validationMessages.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            searchViewModel.isInputValid("")
            val emptyInputEmission = awaitItem()
            assertThat(emptyInputEmission).isEqualTo(listOf("Input cannot be empty"))
        }
    }


    @Test
    fun searchResults_notEmptyInput_returnsNotEmptyList() = runTest(testDispatchers.testDispatcher) {
        searchViewModel.searchResults.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            searchViewModel.onSearchQueryChange(newQuery = "do")

            val emptyListEmission = awaitItem()
            assertThat(emptyListEmission).isNotEmpty()
        }
    }

    @Test
    fun searchResults_notEmptyInput_returnsCorrectOutput() = runTest {
        searchViewModel.searchResults.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            searchViewModel.onSearchQueryChange(newQuery = "do")

            val emptyListEmission = awaitItem()
            assertThat(emptyListEmission).isNotEmpty()
            assertThat(emptyListEmission).isEqualTo(listOf("Shadow", "Fido", "Shadowfax", "Dottie"))
        }
    }

    // This is failing and I don't know why this is failing.
    @Test
    fun validationMessages_validInput_returnsEmptyList() = runTest {
        searchViewModel.validationMessages.test {
            val initialEmission = awaitItem()
            assertThat(initialEmission).isEmpty()

            searchViewModel.isInputValid("K")
            advanceTimeBy(4050)

            val emptyListEmission = awaitItem()
            assertThat(emptyListEmission).isEqualTo(emptyList<String>())
        }
    }
}

