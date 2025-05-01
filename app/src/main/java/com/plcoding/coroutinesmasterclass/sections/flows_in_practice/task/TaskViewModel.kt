package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.coroutinesmasterclass.util.db.TaskDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
class TaskViewModel(
    private val taskDao: TaskDao
): ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    /**
     * Little note about the difference between flatMapConcat, flatMapLatest and flatMapMerge
     * 1. flatMaoConcat - This will process emission sequentially, one by one, from the first flow emission to the last
     * 2. flatMapLatest - This will process the latest flow emission and cancel the previous one
     * 3. flatMapMerge - This will process all flow emissions concurrently
     *
     * When to use what
     * 1. flatMapConcat - When you want to process the emissions sequentially and you care about all emissions
     * 2. flatMapLatest - When you want to process the latest emission and cancel the previous one
     * 3. flatMapMerge - When you want to process all emissions concurrently and you don't care about the order but all the emissions
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    val tasks = selectedDate
        .map { date ->
            val startMillis = date
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000L
            val endMillis = date
                .plusDays(1)
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000L

            startMillis to endMillis
        }
        .flatMapLatest { (startMillis, endMillis) ->
            taskDao.getTasksBetween(startMillis, endMillis)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

}