package com.hupu.games

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.lamps.sdk.LampsSdk

/**
 * Routes links opened by the in-app scanner or an external VIEW intent.
 * `http` / `https` open in the SDK web page. `huputiyu://` stays in-app.
 * Add a new branch in [dispatch] when another schema needs to be supported.
 */
internal object DemoSchemaDispatcher {

    const val SCHEME = "huputiyu"
    private const val COMMON_WEB_VIEW_ACTIVITY = "com.lamps.sdk.webview.CommonWebViewActivity"
    private const val EXTRA_URL = "extra_url"

    fun dispatch(activity: Activity, raw: String) {
        val text = raw.trim()
        if (text.isEmpty()) {
            toast(activity, R.string.schema_empty)
            return
        }
        val uri = Uri.parse(text)
        val scheme = uri.scheme
        if (scheme.equals("http", ignoreCase = true) || scheme.equals("https", ignoreCase = true)) {
            openWeb(activity, uri)
            return
        }
        if (!SCHEME.equals(scheme, ignoreCase = true)) {
            toast(activity, R.string.schema_unrecognized)
            return
        }
        when (routeOf(uri)) {
            "ad/lamps/game" -> openGame(activity, uri)
            else -> toast(activity, R.string.schema_unsupported)
        }
    }

    private fun openWeb(activity: Activity, uri: Uri) {
        val intent = Intent().setClassName(activity, COMMON_WEB_VIEW_ACTIVITY).apply {
            putExtra(EXTRA_URL, uri.toString())
        }
        try {
            activity.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            toast(activity, R.string.schema_unsupported)
        }
    }

    private fun openGame(activity: Activity, uri: Uri) {
        val gameId = uri.getQueryParameter("gameId")?.trim().orEmpty()
        if (gameId.isEmpty()) {
            toast(activity, R.string.schema_game_id_empty)
            return
        }
        LampsSdk.navigateToGame(activity, gameId)
    }

    private fun routeOf(uri: Uri): String {
        val host = uri.host.orEmpty().trim('/')
        val path = uri.path.orEmpty().trim('/')
        return if (path.isEmpty()) host else "$host/$path"
    }

    private fun toast(activity: Activity, messageRes: Int) {
        Toast.makeText(activity, messageRes, Toast.LENGTH_SHORT).show()
    }
}
