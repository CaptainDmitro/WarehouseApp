package ru.captaindmitro.warehouseapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceImplTest {

    @Mock
    private lateinit var warehouseApi: WarehouseApi
    private lateinit var remoteDataSource: RemoteDataSource
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        remoteDataSource = RemoteDataSourceImpl(warehouseApi, dispatcher)
    }

    @Test
    fun `Test RemoteDataSource returns list of ProductResponse`() = runTest {
        `when`(warehouseApi.getProducts()).thenReturn(
            List(10) { ProductResponse(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble()) }
        )

        val expected = List(10) { ProductResponse(it, "$it", "Test $it", "Check $it", it.toDouble(), it.toDouble()) }
        val actual = remoteDataSource.getProducts()

        assertEquals(expected, actual)
    }

}