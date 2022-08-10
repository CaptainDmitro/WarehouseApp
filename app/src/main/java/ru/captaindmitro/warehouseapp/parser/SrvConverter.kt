package ru.captaindmitro.warehouseapp.parser

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import ru.captaindmitro.warehouseapp.data.ProductResponse
import java.lang.reflect.Type

class SrvConverter : Converter<ResponseBody, List<ProductResponse>> {
    private val srvParser = SrvParser()

    override fun convert(value: ResponseBody): List<ProductResponse>? {
        try {
            val res = srvParser.read(value.charStream())
            return res
        } finally {
            value.close()
        }
    }

}

class SrvConverterFactory private constructor() : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return SrvConverter()
    }

    companion object {
        fun create() = SrvConverterFactory()
    }
}