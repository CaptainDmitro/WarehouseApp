package ru.captaindmitro.warehouseapp.parser

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.captaindmitro.warehouseapp.data.ProductResponse
import java.io.StringReader

class SrvParserTest {
    private val srvParser = SrvParser()

    companion object {
        val EXPECTED_PRODUCT_WITH_ATTRS = ProductResponse(20, "4603928005223", "Водка особая \"Государев заказ на черной смородине\"0,5л", "Водка особая \"Государев заказ на черной смородине\"0,5л", 327.00, 3.000, 200, 40.00)
        val EXPECTED_PRODUCT_NO_ATTRS = ProductResponse(20, "4603928005223", "Водка особая \"Государев заказ на черной смородине\"0,5л", "Водка особая \"Государев заказ на черной смородине\"0,5л", 327.00, 3.000, null, null)
        val EXPECTED_EMPTY_LIST = listOf<ProductResponse>()
    }

    @Test
    fun `Parsing correct MAIN line`() {
        val msg = """
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;3.000;
            """.trimIndent()
        val reader = StringReader(msg)

        val expected = listOf(EXPECTED_PRODUCT_NO_ATTRS)
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

    @Test
    fun `Parsing incorrect MAIN line`() {
        val msg = """
            A;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;3.000;
            20;B;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;3.000;
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;123;3.000;
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;ABC;3.000;
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;12;
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;ACV;
            """.trimIndent()
        val reader = StringReader(msg)

        val expected = EXPECTED_EMPTY_LIST
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

    @Test
    fun `Parsing correct MAIN and incorrect ATTR lines`() {
        val msg = """
            20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;3.000;
            <goods_attr id="20" attr_id="22">200.1</goods_attr>
            <goods_attr id="20" attr_id="23">0.500</goods_attr>
            <goods_attr id="20" attr_id="24">0</goods_attr>
            <goods_attr id="20" attr_id="25"></goods_attr>
            <goods_attr id="20" attr_id="26"></goods_attr>
            <goods_attr id="20" attr_id="30">0001291000003685740</goods_attr>
            <goods_attr id="20" attr_id="31">Packed</goods_attr>
            <goods_attr id="20" attr_id="32">АП</goods_attr>
            <goods_attr id="21" attr_id="22">200</goods_attr>
            <goods_attr id="21" attr_id="23">0.500</goods_attr>
            <goods_attr id="21" attr_id="24">0</goods_attr>
            <goods_attr id="21" attr_id="25">0276100884</goods_attr>
            <goods_attr id="21" attr_id="26">027545001</goods_attr>
            <goods_attr id="21" attr_id="27">40.00</goods_attr>
            <goods_attr id="21" attr_id="30">0102100000001684879</goods_attr>
            <goods_attr id="21" attr_id="31">Packed</goods_attr>
            <goods_attr id="21" attr_id="32">АП</goods_attr>
            """.trimIndent()
        val reader = StringReader(msg)

        val expected = listOf(EXPECTED_PRODUCT_NO_ATTRS)
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

    @Test
    fun `Parsing correct MAIN and ATTR lines`() {
        val msg = """
                20;4603928005223;Водка особая "Государев заказ на черной смородине"0,5л;Водка особая "Государев заказ на черной смородине"0,5л;327.00;3.000;
                <goods_attr id="20" attr_id="22">200</goods_attr>
                <goods_attr id="20" attr_id="27">40.00</goods_attr>
            """.trimIndent()
        val reader = StringReader(msg)

        val expected = listOf(EXPECTED_PRODUCT_WITH_ATTRS)
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

    @Test
    fun `Parsing garbage returns empty list`() {
        val msg = """
            some;line;12;42134;A;123.23;3434;
            a;s;d;f;f;g;d;bv;;dfg;wer;
            12;12;12;12;12;12;12;12;12;
        """.trimIndent()
        val reader = StringReader(msg)

        val expected = EXPECTED_EMPTY_LIST
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

    @Test
    fun `Parsing ATTRS lines returns empty if MAIN object is absent`() {
        val msg = """
            <goods_attr id="20" attr_id="22">200</goods_attr>
            <goods_attr id="20" attr_id="23">0.500</goods_attr>
            <goods_attr id="20" attr_id="24">0</goods_attr>
            <goods_attr id="20" attr_id="25"></goods_attr>
            <goods_attr id="20" attr_id="26"></goods_attr>
            <goods_attr id="20" attr_id="27">40.00</goods_attr>
        """.trimIndent()
        val reader = StringReader(msg)

        val expected = EXPECTED_EMPTY_LIST
        val actual = srvParser.read(reader)

        assertEquals(expected, actual)
    }

}