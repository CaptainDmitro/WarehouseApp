package ru.captaindmitro.warehouseapp.parser

import ru.captaindmitro.warehouseapp.data.ProductResponse
import java.io.Reader

interface Parser<T> {

    fun parse(reader: Reader): List<T>

}

class NewSrvParser: Parser<ProductResponse> {
    private val srvRulesChain: RulesChain = RulesChain.Base(
        """(\d*);(\d*);([^;]*);([^;]*);[-+]?([0-9]*\.[0-9]+);[-+]?([0-9]*\.[0-9]+);""".toRegex(),
        """<goods_attr id=\"(\d*)\" attr_id=\"(22|27)\">[-+]?([0-9]*\.[0-9]+|[0-9]+)<\/goods_attr>""".toRegex()
    )

    private val products = mutableMapOf<Int, ProductResponse>()

    override fun parse(reader: Reader): List<ProductResponse> {
        reader.forEachLine { line ->
            val groups = srvRulesChain.run(line)
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
