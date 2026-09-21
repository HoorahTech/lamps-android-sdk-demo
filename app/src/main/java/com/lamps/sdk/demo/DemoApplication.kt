package com.lamps.sdk.demo

import android.app.Application
import android.util.Log
import com.lamps.sdk.LampsSdk
import com.lamps.sdk.config.LampsConfig
import com.lamps.sdk.core.InitCallback
import java.util.concurrent.CopyOnWriteArrayList

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val accepted = LampsSdk.init(
            this,
            LampsConfig.Builder()
                .appId(DEMO_APP_ID)
                .setOaidProvider { DEMO_OAID }
                .setDebug(true)
                .setLampsInitPangleSdk(true)
                .setLampsInitYlhSdk(true)
                .setLampsInitNoahSdk(true)
                .setCustomData(mapOf("source" to "lamps-android-sdk-demo"))
                .build()
        )
        Log.i(TAG, "init accepted=$accepted")

        LampsSdk.startAsync(object : InitCallback {
            override fun success() {
                lastStartError = null
                Log.i(TAG, "start success, ready=${LampsSdk.isSdkReady()} version=${LampsSdk.getSdkVersion()}")
                notifySdkStatus()
            }

            override fun fail(code: Int, message: String?) {
                lastStartError = "code=$code message=$message"
                Log.e(TAG, "start fail $lastStartError")
                notifySdkStatus()
            }
        })
    }

    companion object {
        const val TAG = "LampsDemo"
        const val DEMO_APP_ID = "fyei9kd1wt1c"
        const val DEMO_OAID = "af417ed4dc21da66b186505e4b63723ffa80dfd579cf0c657d1cd6137aa496ef"

        @Volatile
        var lastStartError: String? = null
            private set

        private val statusListeners = CopyOnWriteArrayList<() -> Unit>()

        fun addSdkStatusListener(listener: () -> Unit) {
            statusListeners.add(listener)
            listener()
        }

        fun removeSdkStatusListener(listener: () -> Unit) {
            statusListeners.remove(listener)
        }

        private fun notifySdkStatus() {
            statusListeners.forEach { it() }
        }
    }
}
