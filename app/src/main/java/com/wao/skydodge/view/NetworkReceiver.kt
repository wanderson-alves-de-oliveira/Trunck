package com.wao.skydodge.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class NetworkReceiver(private val onNetworkAvailable: () -> Unit) : BroadcastReceiver() {
    var temInternet = false
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && isInternetAvailable(context)) {
            Log.d("NetworkReceiver", "Internet voltou! Recarregando anúncio...")
            onNetworkAvailable() // Chama a função para recarregar o anúncio
            temInternet = true
        }else{
            temInternet = false

        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}