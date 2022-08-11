package ru.captaindmitro.warehouseapp.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import ru.captaindmitro.warehouseapp.domain.Repository
import ru.captaindmitro.warehouseapp.domain.models.Product
import ru.captaindmitro.warehouseapp.domain.common.Result

@RunWith(MockitoJUnitRunner::class)
class GetProductsUseCaseTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var getProductsUseCase: GetProductsUseCase
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getProductsUseCase = GetProductsUseCase(repository, dispatcher)
    }

    @Test
    fun `Test GetProductsUseCase emits ResultSuccess with data on success`() = runTest {
        Mockito.`when`(repository.getProducts()).thenReturn(
            List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) }
        )

        val expected = Result.Success(List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) })
        val actual = getProductsUseCase.execute().first()

        assertEquals(expected, actual)
    }
}