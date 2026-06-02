package com.example.standardapp.ui.counter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.standardapp.databinding.FragmentCounterBinding
import com.example.standardapp.ui.dialogs.SampleDialogFragment
import androidx.core.content.edit

class CounterFragment : Fragment() {

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!

    private val prefs by lazy {
        requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateCountText()
        binding.openDialogButton.setOnClickListener {
            val nextCount = prefs.getInt(KEY_CLICK_COUNT, 0) + 1
            prefs.edit {
                putInt(KEY_CLICK_COUNT, nextCount)
            }
            updateCountText(nextCount)
            SampleDialogFragment().show(childFragmentManager, "sample")
        }
    }

    private fun updateCountText(count: Int = prefs.getInt(KEY_CLICK_COUNT, 0)) {
        binding.clickCountText.text = "An +1: $count luot da an"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PREFS_NAME = "counter_prefs"
        private const val KEY_CLICK_COUNT = "dialog_click_count"
    }
}
