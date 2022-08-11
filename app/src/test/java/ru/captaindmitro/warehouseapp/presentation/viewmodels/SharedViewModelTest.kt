package ru.captaindmitro.warehouseapp.presentation.viewmodels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import ru.captaindmitro.warehouseapp.data.ProductResponse
import ru.captaindmitro.warehouseapp.domain.common.Result
import ru.captaindmitro.warehouseapp.domain.models.Product
import ru.captaindmitro.warehouseapp.domain.usecases.GetProductsUseCase
import ru.captaindmitro.warehouseapp.presentation.common.UiState

@RunWith(MockitoJUnitRunner::class)
class SharedViewModelTest {
    @Mock
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var sharedViewModel: SharedViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        sharedViewModel = SharedViewModel(getProductsUseCase)
    }

    @Test
    fun `SharedViewModel products represents UiState Empty if usecase returns empty data in Result Success`() = runTest {
        Mockito.`when`(getProductsUseCase.execute()).thenReturn(
            flowOf(
                Result.Success(
                    emptyList()
                )
            )
        )

        sharedViewModel.getProducts()

        delay(1000)

        val expected = UiState.Empty
        val actual = sharedViewModel.products.first()

        assertEquals(expected, actual)
    }


    @Test
    fun `SharedViewModel products represents UiState Loading as an initial state of collection products`() = runTest {
        Mockito.`when`(getProductsUseCase.execute()).thenReturn(
            flowOf(
                Result.Success(
                    List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) }
                )
            )
        )

        sharedViewModel.getProducts()

        val expected = UiState.Loading
        val actual = sharedViewModel.products.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `SharedViewModel products represents UiState Success on success collecting`() = runTest {
        Mockito.`when`(getProductsUseCase.execute()).thenReturn(
            flowOf(
                Result.Success(
                    List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) }
                )
            )
        )

        sharedViewModel.getProducts()

        delay(1000)

        val expected = UiState.Success(List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) })
        val actual = sharedViewModel.products.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `SharedViewModel products represents UiState Error on error in collecting`() = runTest {
        Mockito.`when`(getProductsUseCase.execute()).thenReturn(
            flowOf(
                Result.Error(Exception("Test exception"))
            )
        )

        sharedViewModel.getProducts()

        delay(1000)

        val expected = UiState.Error(Exception("Test exception")).exception.message
        val actual = (sharedViewModel.products.first() as UiState.Error).exception.message

        assertEquals(expected, actual)
    }
}