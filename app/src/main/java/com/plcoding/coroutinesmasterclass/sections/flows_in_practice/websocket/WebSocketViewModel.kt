@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.websocket

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.coroutinesmasterclass.util.api.HttpClientFactory
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.roundToInt

data class WebSocketLog(
    val formattedTime: String,
    val log: String
)

class WebSocketViewModel: ViewModel() {

    private val client = WebSocketClient(HttpClientFactory.create())

    @RequiresApi(Build.VERSION_CODES.O)
    val receivedLogs = client
        .listenToSocket("wss://echo.websocket.org/")
        // Error handling in flow - we can also retry for some cases as well
        .retryWhen { cause, attempt ->
            // we can suspend here - wait
            //delay(3000L)
            delay(2f.pow(attempt.toInt()).roundToInt() * 2000L)
            cause is UnresolvedAddressException && attempt < 3
        }
        .catch { cause ->
            when (cause) {
                is UnknownHostException -> {

                }
                is UnresolvedAddressException -> {
                    println("Opps!, no internet connection")
                }
            }
        }
        .flatMapMerge {
            flow {
                processLog()
                emit(it)
            }
        }
        .runningFold(initial = emptyList<WebSocketLog>()) { logs, newLog ->
            val formattedTime = DateTimeFormatter
                .ofPattern("dd-MM-yyyy, hh:mm:ss")
                .format(LocalDateTime.now())

            logs + WebSocketLog(
                formattedTime = formattedTime,
                log = newLog
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    fun sendMessage(text: String) {
        viewModelScope.launch {
            client.sendMessage(text)
        }
    }

    private suspend fun processLog() {
        delay(3000L)
    }
}