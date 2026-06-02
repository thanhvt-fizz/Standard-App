package com.example.standardapp.ui.first

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
import com.example.standardapp.data.FirstItem
import com.example.standardapp.data.FirstListViewUiState
import com.example.standardapp.databinding.FragmentFirstListBinding
import com.example.standardapp.ui.common.TextRow
import com.example.standardapp.ui.common.TextRowAdapter
import com.example.standardapp.ui.shared.DetailContent
import com.example.standardapp.ui.shared.SharedItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstListFragment : Fragment() {

    private var _binding: FragmentFirstListBinding? = null
    private val binding get() = _binding!!

    private val vm: FirstListViewModel by viewModels()
    private val sharedVm: SharedItemViewModel by activityViewModels()
    private lateinit var adapter: TextRowAdapter<FirstItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TextRowAdapter(
            rowMapper = { item ->
                TextRow(
                    title = item.title,
                    body = item.description,
                    meta = "ID: ${item.id} | Type: ${item.type}"
                )
            },
            onItemClick = { item ->
                sharedVm.selectDetail(
                    DetailContent(
                        title = item.title,
                        description = item.description,
                        metadata = "JSON 1 | ID: ${item.id} | Type: ${item.type}"
                    )
                )
                findNavController().navigate(R.id.action_home_to_detail)
            }
        )

        binding.firstRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.firstRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collectLatest { state ->
                    when (state) {
                        is FirstListViewUiState.Loading -> {
                            binding.firstStatusText.text = "Loading JSON 1..."
                            adapter.submitItems(emptyList())
                        }
                        is FirstListViewUiState.Success -> {
                            binding.firstStatusText.text = "JSON 1 list"
                            adapter.submitItems(state.items)
                        }
                        is FirstListViewUiState.Error -> {
                            binding.firstStatusText.text = "Error: ${state.message}"
                            adapter.submitItems(emptyList())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.firstRecyclerView.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
