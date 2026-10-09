package com.hupu.games

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import com.lamps.sdk.LampsSdk

/**
 * Routes `huputiyu://` links opened by the in-app scanner or an external VIEW intent.
 * Add a new branch in [dispatch] when another schema needs to be supported.
 */
internal object DemoSchemaDispatcher {

    const val SCHEME = "huputiyu"

    fun dispatch(activity: Activity, raw: String) {
        val text = raw.trim()
        if (text.isEmpty()) {
            toast(activity, R.string.schema_empty)
            return
        }
        val uri = Uri.parse(text)
        if (!SCHEME.equals(uri.scheme, ignoreCase = true)) {
            toast(activity, R.string.schema_unrecognized)
            return
        }
        when (routeOf(uri)) {
            "ad/lamps/game" -> openGame(activity, uri)
            else -> toast(activity, R.string.schema_unsupported)
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
