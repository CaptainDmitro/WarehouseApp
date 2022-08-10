package ru.captaindmitro.warehouseapp.parser

interface ParseStrategy {
    fun parse(line: String): List<String>?

    abstract class Base(
        private val regex: Regex,
        private val onFailure: (String) -> List<String>?
    ) : ParseStrategy {

        override fun parse(line: String): List<String>? {
            if (regex.containsMatchIn(line)) {
                return regex.find(line)?.groupValues?.drop(1)
            }
            return onFailure(line)
        }

    }
}

class MainParseStrategy(
    regex: Regex,
    onFailure: (String) -> List<String>?
) : ParseStrategy.Base(regex, onFailure)

class AttrParseStrategy(
    regex: Regex,
    onFailure: (String) -> List<String>?
) : ParseStrategy.Base(regex, onFailure)