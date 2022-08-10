package ru.captaindmitro.warehouseapp.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.captaindmitro.warehouseapp.di.IoDispatcher
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val warehouseApi: WarehouseApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RemoteDataSource {

    override suspend fun getProducts(): List<ProductResponse> = withContext(dispatcher) {
        warehouseApi.getProducts()
    }

}