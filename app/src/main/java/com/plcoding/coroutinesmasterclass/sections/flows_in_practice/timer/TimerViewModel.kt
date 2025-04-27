package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import kotlin.math.min

class TimerViewModel: ViewModel() {

    /**
     * This runningReduce operator behaves like a fibinacci sequence.
     * flowOf(1, 2, 3, 4).runningReduce { acc, value -> acc + value }.toList()
     * will give you [1, 3, 6, 10]
     * A practical scenario of runningReduce is to get all the flow emissions in a big list
     */
    val formattedTime = timeAndEmit(10f)
        .runningReduce { totalElapsedTime, newElapsedTime ->
            totalElapsedTime + newElapsedTime
        }
        .map { totalElapsedTime ->
            totalElapsedTime.toComponents { hours, minutes, seconds, nanoseconds ->
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds,
                    nanoseconds / (1_000_000L * 10L)
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            "00:00:00:00"
        )

    private val totalProgressTimeMillis = 5000L
    val progress = timeAndEmit(100f)
        .runningReduce { totalElapsedTime, newElapsedTime ->
            totalElapsedTime + newElapsedTime
        }
        .map { totalDuration ->
            (totalDuration.inWholeMilliseconds / totalProgressTimeMillis.toFloat())
                .coerceIn(0f, 1f)
        }
        .filter { progressFraction ->
            progressFraction in (0f..1f)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            0f
        )
}