package com.hupu.games

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.lamps.sdk.LampsSdk

class MainActivity : ComponentActivity() {

    private lateinit var statusText: TextView
    private val onSdkStatusChanged = { refreshStatus() }
    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        val contents = result.contents ?: return@registerForActivityResult
        DemoSchemaDispatcher.dispatch(this, contents)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.sdkStatusText)
        SdkToolsBinder.bind(this, findViewById(R.id.openSdkToolsButton))

        findViewById<Button>(R.id.scanSchemaButton).setOnClickListener {
            scanLauncher.launch(scanOptions())
        }

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

        if (savedInstanceState == null) {
            dispatchViewIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        dispatchViewIntent(intent)
    }

    private fun scanOptions(): ScanOptions {
        return ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt(getString(R.string.scan_schema_prompt))
            setBeepEnabled(false)
            setOrientationLocked(true)
        }
    }

    private fun dispatchViewIntent(intent: Intent?) {
        if (intent?.action != Intent.ACTION_VIEW) return
        val raw = intent.dataString ?: return
        DemoSchemaDispatcher.dispatch(this, raw)
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
