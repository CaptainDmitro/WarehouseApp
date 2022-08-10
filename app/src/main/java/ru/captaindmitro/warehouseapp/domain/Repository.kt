package ru.captaindmitro.warehouseapp.domain

import ru.captaindmitro.warehouseapp.domain.models.Product

interface Repository {

    suspend fun getProducts(): List<Product>

}