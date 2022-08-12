package ru.captaindmitro.warehouseapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

        init()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        homeAdapter = HomeAdapter(sharedViewModel::setSelectedProduct).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.homeRecycleView.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.products.collectLatest { result ->
                        when (result) {
                            is UiState.Loading -> {
                                binding.loadingIndicator.show()
                            }
                            is UiState.Empty -> {
                                binding.loadingIndicator.isGone = true
                            }
                            is UiState.Success -> {
                                binding.loadingIndicator.isGone = true
                                homeAdapter.setData(result.data)
                            }
                            is UiState.Error -> {
                                Snackbar.make(binding.root, "${result.exception}", Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getString(R.string.update)) { sharedViewModel.getProducts() }
                                    .show()
                                binding.loadingIndicator.isGone = true
                            }
                        }
                    }
                }
            }
        }
    }
}