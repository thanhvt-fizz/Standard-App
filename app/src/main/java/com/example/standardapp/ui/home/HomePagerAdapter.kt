package com.example.standardapp.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.standardapp.ui.counter.CounterFragment
import com.example.standardapp.ui.first.FirstListFragment
import com.example.standardapp.ui.media.MediaFragment
import com.example.standardapp.ui.second.SecondListFragment

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_JSON_ONE -> FirstListFragment()
            PAGE_JSON_TWO -> SecondListFragment()
            PAGE_MEDIA -> MediaFragment()
            PAGE_COUNTER -> CounterFragment()
            else -> FirstListFragment()
        }
    }

    companion object {
        const val PAGE_JSON_ONE = 0
        const val PAGE_JSON_TWO = 1
        const val PAGE_MEDIA = 2
        const val PAGE_COUNTER = 3
        const val PAGE_COUNT = 4
    }
}
