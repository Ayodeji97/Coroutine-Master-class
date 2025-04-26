@file:OptIn(ExperimentalCoroutinesApi::class)

package com.plcoding.coroutinesmasterclass

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.coroutinesmasterclass.sections.flows_in_practice.assignment.NetworkChecker
import com.plcoding.coroutinesmasterclass.sections.flows_in_practice.assignment.ShowSnackBar
import com.plcoding.coroutinesmasterclass.sections.flows_in_practice.backpressure.backpressureDemo
import com.plcoding.coroutinesmasterclass.sections.testing.search.SearchScreen
import com.plcoding.coroutinesmasterclass.sections.testing.search.SearchViewModel
import com.plcoding.coroutinesmasterclass.ui.theme.CoroutinesMasterclassTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            0
        )

       // backpressureDemo()

        setContent {
            CoroutinesMasterclassTheme {
//                Scaffold { innerPadding ->
//                    WebSocketUi(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }

              /**
                val viewModel: SearchViewModel = viewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                SearchScreen(
                    state = state,
                    onSearchTextChange = viewModel::onSearchQueryChange
                )
                */
              var hasInternetConnection by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(hasInternetConnection) {
                    NetworkChecker(this@MainActivity)
                        .isDeviceConnected()
                        .onEach {
                            hasInternetConnection = it
                        }
                        .launchIn(lifecycleScope)
                }

                Scaffold {
                    innerPadding ->
                    ShowSnackBar(
                        hasInternetConnection = hasInternetConnection,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.R)
suspend fun Context.getLocation(): Location {
    return suspendCancellableCoroutine { continuation ->
        val locationManager = getSystemService<LocationManager>()!!

        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(
            this@getLocation,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocationPermission = ActivityCompat.checkSelfPermission(
            this@getLocation,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val signal = CancellationSignal()
        if(hasFineLocationPermission && hasCoarseLocationPermission) {
            locationManager.getCurrentLocation(
                LocationManager.NETWORK_PROVIDER,
                signal,
                mainExecutor
            ) { location ->
                println("Got location: $location")
                continuation.resume(location)
            }
        } else {
            continuation.resumeWithException(
                RuntimeException("Missing location permission")
            )
        }

        continuation.invokeOnCancellation {
            signal.cancel()
        }
    }
}