package ru.captaindmitro.warehouseapp.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.captaindmitro.warehouseapp.domain.common.Result
import ru.captaindmitro.warehouseapp.domain.models.Product
import ru.captaindmitro.warehouseapp.domain.usecases.GetProductsUseCase
import ru.captaindmitro.warehouseapp.presentation.common.UiState
import javax.inject.Inject

private const val SELECTED_PRODUCT_KEY = "SELECTED_PRODUCT"

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _products: MutableStateFlow<UiState<List<Product>>> = MutableStateFlow(UiState.Empty)
    val products = _products.asStateFlow()

//    private val _selectedProduct: MutableStateFlow<Product?> = MutableStateFlow(null)
//    val selectedProduct = _selectedProduct.asStateFlow()
    val selectedProduct: StateFlow<Product?> = state.getStateFlow(SELECTED_PRODUCT_KEY, null)

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = UiState.Loading
            getProductsUseCase.execute().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _products.value = if (result.data.isEmpty()) UiState.Empty else UiState.Success(result.data)
                    }
                    is Result.Error -> {
                        _products.value = UiState.Error(result.exception)
                    }
                }
            }
        }
    }

    fun setSelectedProduct(position: Int) {
//        _selectedProduct.value = (products.value as UiState.Success).data[position]
        state[SELECTED_PRODUCT_KEY] = (products.value as UiState.Success).data[position]
    }

}