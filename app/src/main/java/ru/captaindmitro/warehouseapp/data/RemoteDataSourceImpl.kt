package ru.captaindmitro.warehouseapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl constructor(
    private val warehouseApi: WarehouseApi,
    private val dispatcher: CoroutineDispatcher
) : RemoteDataSource {

    override suspend fun getProducts(): List<ProductResponse> = withContext(dispatcher) {
        warehouseApi.getProducts()
    }

}