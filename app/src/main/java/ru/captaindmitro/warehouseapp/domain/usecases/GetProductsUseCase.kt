package ru.captaindmitro.warehouseapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.captaindmitro.warehouseapp.domain.Repository
import ru.captaindmitro.warehouseapp.domain.common.Result
import ru.captaindmitro.warehouseapp.domain.models.Product

class GetProductsUseCase constructor(
    private val repository: Repository
) {

    fun execute(): Flow<Result<List<Product>>> = flow {
        try {
            val res = repository.getProducts()
            emit(Result.Success(res))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

}