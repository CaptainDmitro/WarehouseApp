package ru.captaindmitro.warehouseapp.data

interface RemoteDataSource {

    suspend fun getProducts(): List<ProductResponse>

}