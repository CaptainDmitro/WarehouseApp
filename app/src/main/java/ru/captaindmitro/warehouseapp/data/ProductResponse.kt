package ru.captaindmitro.warehouseapp.data

import ru.captaindmitro.warehouseapp.domain.models.Product

data class ProductResponse(
    val code: Int,
    val barcode: String,
    val name: String,
    val checkName: String,
    val price: Double,
    val remains: Double,
    var type: Int? = null,
    var alc: Double? = null
)

fun ProductResponse.toDomain() = Product(
    this.code,
    this.barcode,
    this.name,
    this.checkName,
    this.price,
    this.remains,
    this.type,
    this.alc
)