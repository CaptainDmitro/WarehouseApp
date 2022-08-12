package ru.captaindmitro.warehouseapp.domain.models

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val code: Int,
    val barcode: String,
    val name: String,
    val checkText: String,
    val price: Double,
    val remain: Double,
    val type: Int?,
    val alc: Double?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(barcode)
        parcel.writeString(name)
        parcel.writeString(checkText)
        parcel.writeDouble(price)
        parcel.writeDouble(remain)
        parcel.writeValue(type)
        parcel.writeValue(alc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}