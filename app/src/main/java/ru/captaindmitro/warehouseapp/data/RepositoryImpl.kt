package ru.captaindmitro.warehouseapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.captaindmitro.warehouseapp.di.DefaultDispatcher
import ru.captaindmitro.warehouseapp.domain.Repository
import ru.captaindmitro.warehouseapp.domain.models.Product
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : Repository {

    override suspend fun getProducts(): List<Product> = withContext(dispatcher) {
        remoteDataSource.getProducts().map { productApi -> productApi.toDomain() }
    }

}