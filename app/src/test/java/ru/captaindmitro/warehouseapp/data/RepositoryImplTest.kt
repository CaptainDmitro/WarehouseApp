package ru.captaindmitro.warehouseapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@RunWith(MockitoJUnitRunner::class)
class RepositoryImplTest {
    @Mock
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: Repository
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = RepositoryImpl(remoteDataSource, dispatcher)
    }

    @Test
    fun `Test Repository maps ProductResponse to Product an returns list of Product`() = runTest {
        Mockito.`when`(remoteDataSource.getProducts()).thenReturn(
            List(10) { ProductResponse(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble()) }
        )

        val expected = List(10) { Product(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble(), null, null) }
        val actual = repository.getProducts()

        assertEquals(expected, actual)
    }
}