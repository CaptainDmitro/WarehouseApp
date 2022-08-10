package ru.captaindmitro.warehouseapp.parser

import ru.captaindmitro.warehouseapp.data.ProductResponse
import java.io.Reader

class SrvParser {
    private val attrStrategy = AttrParseStrategy(
        """<goods_attr id=\"(\d*)\" attr_id=\"(22|27)\">[-+]?([0-9]*\.[0-9]+)<\/goods_attr>""".toRegex()
    ) { null }

    private val mainParseStrategy = MainParseStrategy(
        """(\d*);(\d*);([^;]*);([^;]*);[-+]?([0-9]*\.[0-9]+);[-+]?([0-9]*\.[0-9]+);""".toRegex()
    ) { line -> attrStrategy.parse(line) }

    private val products = mutableMapOf<Int, ProductResponse>()

    fun read(reader: Reader): List<ProductResponse> {
        reader.forEachLine { line ->
            val groups = mainParseStrategy.parse(line)
            when (groups?.size) {
                3 -> try {
                    val code = groups[0].toInt()
                    val type = groups[1]
                    val value = groups[2]
                    if (products.contains(code)) {
                        if (type == "22") products[code]?.type = value.toInt()
                        if (type == "27") products[code]?.alc = value.toDouble()
                    }
                } catch (e: Exception) {
                    // Catch exceptions
                }
                6 -> try {
                    val code = groups[0].toInt()
                    val barcode = groups[1]
                    val name = groups[2]
                    val checkName = groups[3]
                    val price = groups[4].toDouble()
                    val remains = groups[5].toDouble()

                    products[code] = ProductResponse(code, barcode, name, checkName, price, remains)
                } catch (e: Exception) {
                    // Catch exceptions
                }
            }
        }

        return products.values.toList()
    }
}
