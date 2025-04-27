@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass.sections.flow_fundamentals.assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CountDownViewModel : ViewModel() {

    private val _counterFieldValue = MutableStateFlow("")
    val counterFieldValue = _counterFieldValue.asStateFlow()

    private val _countDownValue = MutableStateFlow(0)
    val countDownValue = _countDownValue.asStateFlow()

    private val startCountDown = MutableStateFlow(0)


    fun onValueChange(newValue: String) {
        _counterFieldValue.value = newValue
    }

//    fun onCountDownAction() {
//        countDown(counter = _counterFieldValue.value.toInt())
//            .onEach {
//                _countDownValue.value = it
//            }
//            .launchIn(viewModelScope)
//    }

    val countDown1: StateFlow<Int> = flow {
        while (_countDownValue.value > 0) {
            delay(1000L)
            emit(_countDownValue.value)
            _countDownValue.update {
                it - 1
            }
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = 0
    )

    val countDown: StateFlow<Int> = startCountDown
        .flatMapLatest {
            flow {
                var current = it
                while (current > 0) {
                    delay(1000L)
                    current--
                    emit(current)
                }
            }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = 0
        )



    fun startCountDown() {
        viewModelScope.launch {
            startCountDown.emit(_counterFieldValue.value.toIntOrNull() ?: 0)
        }
    }


    private fun countDown(counter: Int): Flow<Int> {
        var countDownValue = counter
        val mutex = Mutex()
        return flow {
            emit(countDownValue)
            repeat(countDownValue) {
                delay(1000L)
                mutex.withLock {
                    countDownValue--
                }
                emit(countDownValue)
            }
        }
    }
}