package ru.captaindmitro.warehouseapp.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.captaindmitro.warehouseapp.di.IoDispatcher
import ru.captaindmitro.warehouseapp.domain.Repository
import ru.captaindmitro.warehouseapp.domain.common.Result
import ru.captaindmitro.warehouseapp.domain.models.Product
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: Repository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun execute(): Flow<Result<List<Product>>> = flow {
        try {
            val res = repository.getProducts()
            emit(Result.Success(res))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(dispatcher)

}