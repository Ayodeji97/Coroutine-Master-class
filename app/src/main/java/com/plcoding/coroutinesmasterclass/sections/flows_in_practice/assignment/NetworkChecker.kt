package com.plcoding.coroutinesmasterclass.sections.flows_in_practice.assignment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkChecker(
    context: Context
) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isDeviceConnected(): Flow<Boolean> {
        return callbackFlow {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    trySend(true)
                }

                override fun onLost(network: android.net.Network) {
                    trySend(false)
                }
            }

            connectivityManager.requestNetwork(networkRequest, networkCallback)

            awaitClose {
                println("Flow was closed.")
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}


//val networkCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onAvailable(network: android.net.Network) {
//                    trySend(true).isSuccess
//                }
//
//                override fun onLost(network: android.net.Network) {
//                    trySend(false).isSuccess
//                }
//            }
//            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//            awaitClose {
//                connectivityManager.unregisterNetworkCallback(networkCallback)
//            }