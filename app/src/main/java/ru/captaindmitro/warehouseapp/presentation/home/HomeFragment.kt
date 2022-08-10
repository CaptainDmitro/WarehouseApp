package ru.captaindmitro.warehouseapp.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.captaindmitro.warehouseapp.R
import ru.captaindmitro.warehouseapp.databinding.FragmentHomeBinding
import ru.captaindmitro.warehouseapp.presentation.common.UiState
import ru.captaindmitro.warehouseapp.presentation.home.adapter.HomeAdapter
import ru.captaindmitro.warehouseapp.presentation.viewmodels.SharedViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeAdapter = HomeAdapter().apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.homeRecycleView.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                sharedViewModel.product.collectLatest { result ->
                    when (result) {
                        is UiState.Loading -> {
                            Log.i("Main", "Loading...")
                            binding.loadingIndicator.show()
                        }
                        is UiState.Empty -> {
                            Log.i("Main", "Empty")
                            binding.loadingIndicator.hide()
                        }
                        is UiState.Success -> {
                            Log.i("Main", "Success: ${result.data.size}")
                            binding.loadingIndicator.hide()
                            homeAdapter.setData(result.data)
                        }
                        is UiState.Error -> {
                            Log.i("Main", "Error: ${result.exception}")
                            binding.loadingIndicator.hide()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}