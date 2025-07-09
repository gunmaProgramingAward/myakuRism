package com.example.myaku_rismu.core.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor() : ViewModel() {

    fun checkStatus(context: Context) {
        val providerPackageName = "com.google.android.apps.healthdata"
        val status = HealthConnectClient.getSdkStatus(context, providerPackageName)
        if (status == HealthConnectClient.SDK_UNAVAILABLE) {
            return
        }
        if (status == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            val uriString = "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
            context.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", context.packageName)
                }
            )
            return
        }
    }
}