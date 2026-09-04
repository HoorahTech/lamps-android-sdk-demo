package com.lamps.sdk.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DemoTabActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_tab)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager)
        viewPager.adapter = TabPagerAdapter()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TAB_NAMES[position]
        }.attach()
    }

    companion object {
        private val TAB_NAMES = listOf("热门推荐", "游戏中心", "最新资讯", "精选内容", "我的关注")
    }

    private inner class TabPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = TAB_NAMES.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                1 -> GameCenterTabFragment()
                else -> SimpleListFragment.newInstance(
                    title = TAB_NAMES[position],
                    itemCount = 20,
                    gameCenterAtFifth = position == 0
                )
            }
        }
    }
}
