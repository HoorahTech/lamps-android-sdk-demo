package com.lamps.sdk.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lamps.sdk.LampsSdk

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private val onSdkStatusChanged = { refreshStatus() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.sdkStatusText)
        SdkToolsBinder.bind(this, findViewById(R.id.openSdkToolsButton))

        findViewById<Button>(R.id.navigateGameCenterButton).setOnClickListener {
            LampsSdk.navigateToGameCenter(this)
        }

        findViewById<Button>(R.id.navigateGameButton).setOnClickListener {
            val gameId = findViewById<EditText>(R.id.gameIdInput).text?.toString().orEmpty()
            LampsSdk.navigateToGame(this, gameId)
        }

        findViewById<Button>(R.id.getGameCenterFragmentButton).setOnClickListener {
            startActivity(Intent(this, DemoTabActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        DemoApplication.addSdkStatusListener(onSdkStatusChanged)
    }

    override fun onStop() {
        DemoApplication.removeSdkStatusListener(onSdkStatusChanged)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        if (!::statusText.isInitialized) return
        statusText.text = buildStatusText()
    }

    private fun buildStatusText(): String {
        return buildString {
            appendLine("Lamps Android SDK Demo")
            appendLine("版本: ${LampsSdk.getSdkVersion()}")
            appendLine("appId: ${DemoApplication.DEMO_APP_ID}")
            appendLine("ready: ${LampsSdk.isSdkReady()}")
            DemoApplication.lastStartError?.let { error ->
                appendLine("start: fail $error")
            }
            appendLine("渠道: 穿山甲 / 优量汇 / 汇川")
        }
    }
}
