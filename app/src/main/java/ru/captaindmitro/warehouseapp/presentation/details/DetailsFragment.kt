package ru.captaindmitro.warehouseapp.presentation.details

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.captaindmitro.warehouseapp.R
import ru.captaindmitro.warehouseapp.databinding.FragmentDetailsBinding
import ru.captaindmitro.warehouseapp.presentation.viewmodels.SharedViewModel

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    sharedViewModel.selectedProduct.collectLatest { product ->
                        Log.i("Main", "Received product: $product")
                        when (product) {
                            null -> { Snackbar.make(binding.root, "Error loading product", Snackbar.LENGTH_SHORT).show() }
                            else -> {
                                with (binding) {
                                    codeTextView.text = getString(R.string.code_field, product.code)
                                    nameTextView.text = product.name
                                    remainsTextView.text = product.remain.toString()
                                    priceTextView.text = getString(R.string.price_field, product.price)
                                    roznPriceTextView.text = getString(R.string.price_field, product.price * product.remain)
                                    product.type?.let { alcType ->
                                        alcTypeField.visibility = View.VISIBLE
                                        alcTypeTextView.apply { visibility = View.VISIBLE; text = alcType.toString() }
                                    }
                                    product.alc?.let { alcValue ->
                                        alcValueField.visibility = View.VISIBLE
                                        alcValueTextView.apply { visibility = View.VISIBLE; text = getString(R.string.alc_field, alcValue) }
                                    }
                                }


                            }
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