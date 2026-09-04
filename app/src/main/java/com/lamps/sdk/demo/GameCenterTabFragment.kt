package com.lamps.sdk.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.lamps.sdk.LampsSdk
import com.lamps.sdk.view.GameCenterView

class GameCenterTabFragment : Fragment() {

    private var gameCenterView: GameCenterView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FrameLayout(requireActivity())
        val view = LampsSdk.getGameCenterView(requireActivity())
        if (view != null) {
            gameCenterView = view
            root.addView(
                view,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        return root
    }

    override fun onDestroyView() {
        gameCenterView?.destroy()
        gameCenterView = null
        super.onDestroyView()
    }
}
