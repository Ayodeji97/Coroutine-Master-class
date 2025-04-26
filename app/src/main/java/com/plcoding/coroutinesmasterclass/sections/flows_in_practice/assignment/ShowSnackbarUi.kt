package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.assignment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch


@Composable
fun ShowSnackBar(
    hasInternetConnection: Boolean,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(hasInternetConnection) {
        if (!hasInternetConnection) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "No internet connection",
                    duration = SnackbarDuration.Indefinite
                )
            }
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        // Screen content
        Box(
            modifier = modifier
                .padding(paddingValues = contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (hasInternetConnection) {
                Text(text = "Voila! You are back with us!")
            }
        }
    }
}