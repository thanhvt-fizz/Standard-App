package com.example.standardapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.standardapp.R
import com.example.standardapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val pageMenuIds = listOf(
        R.id.pageJsonOne,
        R.id.pageJsonTwo,
        R.id.pageMedia,
        R.id.pageCounter
    )

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.homeBottomBar.selectedItemId = pageMenuIds[position]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homePager.adapter = HomePagerAdapter(this)
        binding.homePager.registerOnPageChangeCallback(pageChangeCallback)

        binding.homeBottomBar.setOnItemSelectedListener { item ->
            val position = pageMenuIds.indexOf(item.itemId)
            if (position >= 0) {
                binding.homePager.currentItem = position
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        binding.homePager.unregisterOnPageChangeCallback(pageChangeCallback)
        binding.homePager.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
