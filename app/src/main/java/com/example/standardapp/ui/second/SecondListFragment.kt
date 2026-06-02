package com.example.standardapp.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.standardapp.R
import com.example.standardapp.data.SecondItem
import com.example.standardapp.data.SecondListViewUiState
import com.example.standardapp.databinding.FragmentSecondBinding
import com.example.standardapp.ui.common.TextRow
import com.example.standardapp.ui.common.TextRowAdapter
import com.example.standardapp.ui.shared.DetailContent
import com.example.standardapp.ui.shared.SharedItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SecondListFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val vm: SecondListViewModel by viewModels()
    private val sharedVm: SharedItemViewModel by activityViewModels()
    private lateinit var adapter: TextRowAdapter<SecondItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TextRowAdapter(
            rowMapper = { repository ->
                TextRow(
                    title = repository.name,
                    body = repository.summary,
                    meta = "Owner: ${repository.owner} | Status: ${repository.status} | Stars: ${repository.stars}"
                )
            },
            onItemClick = { repository ->
                sharedVm.selectDetail(
                    DetailContent(
                        title = repository.name,
                        description = repository.summary,
                        metadata = "JSON 2 | Owner: ${repository.owner} | Status: ${repository.status} | Stars: ${repository.stars}"
                    )
                )
                findNavController().navigate(R.id.action_home_to_detail)
            }
        )

        binding.secondRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.secondRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collectLatest { state ->
                    when (state) {
                        is SecondListViewUiState.Loading -> {
                            binding.secondStatusText.text = "Loading JSON 2..."
                            adapter.submitItems(emptyList())
                        }
                        is SecondListViewUiState.Success -> {
                            binding.secondStatusText.text = "JSON 2 list"
                            adapter.submitItems(state.repositories)
                        }
                        is SecondListViewUiState.Error -> {
                            binding.secondStatusText.text = "Error: ${state.message}"
                            adapter.submitItems(emptyList())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.secondRecyclerView.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
