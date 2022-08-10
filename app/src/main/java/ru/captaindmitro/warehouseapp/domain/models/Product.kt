package ru.captaindmitro.warehouseapp.domain.models

data class Product(
    val code: Int,
    val barcode: String,
    val name: String,
    val checkText: String,
    val price: Double,
    val remain: Double,
    val type: Int?,
    val alc: Double?
)