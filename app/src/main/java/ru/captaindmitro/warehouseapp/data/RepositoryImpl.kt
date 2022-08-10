package ru.captaindmitro.warehouseapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.captaindmitro.warehouseapp.domain.Repository
import ru.captaindmitro.warehouseapp.domain.models.Product

class RepositoryImpl constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : Repository {

    override suspend fun getProducts(): List<Product> = withContext(dispatcher) {
        remoteDataSource.getProducts().map { productApi -> productApi.toDomain() }
    }

}