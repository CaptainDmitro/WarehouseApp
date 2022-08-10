package ru.captaindmitro.warehouseapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.captaindmitro.warehouseapp.domain.common.Result
import ru.captaindmitro.warehouseapp.domain.models.Product
import ru.captaindmitro.warehouseapp.domain.usecases.GetProductsUseCase
import ru.captaindmitro.warehouseapp.presentation.common.UiState
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _products: MutableStateFlow<UiState<List<Product>>> = MutableStateFlow(UiState.Empty)
    val product = _products.asStateFlow()

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

}