package ru.captaindmitro.warehouseapp.parser

interface RulesChain {

    fun run(line: String): List<String>?

    class Base(
        vararg patterns: Regex
    ) : RulesChain {

        private val strategies: MutableList<ParserRule> = mutableListOf()

        init {
            strategies.add(ParserRule.Base("".toRegex()) { null })

            patterns.forEachIndexed { index, regex ->
                strategies.add(
                    ParserRule.Base(regex) { line -> strategies[index].parse(line) }
                )
            }

        }

        override fun run(line: String): List<String>? = strategies.last().parse(line)

    }
}