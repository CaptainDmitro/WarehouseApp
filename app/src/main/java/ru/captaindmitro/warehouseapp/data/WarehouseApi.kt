package ru.captaindmitro.warehouseapp.data

import retrofit2.http.GET

interface WarehouseApi {

    @GET("/test/catalog.spr")
    suspend fun getProducts(): List<ProductResponse>

}