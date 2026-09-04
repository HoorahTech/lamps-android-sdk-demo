package com.lamps.sdk.demo

import android.app.Application
import android.util.Log
import com.lamps.sdk.LampsSdk
import com.lamps.sdk.config.LampsConfig
import com.lamps.sdk.core.InitCallback

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
                Log.i(TAG, "start success, ready=${LampsSdk.isSdkReady()} version=${LampsSdk.getSdkVersion()}")
            }

            override fun fail(code: Int, message: String?) {
                Log.e(TAG, "start fail code=$code message=$message")
            }
        })
    }

    companion object {
        const val TAG = "LampsDemo"
        const val DEMO_APP_ID = "10001"
        const val DEMO_OAID = "demo-oaid-from-media"
    }
}
