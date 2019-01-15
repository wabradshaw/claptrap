package com.wabradshaw.claptrap.generation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * A DeterminerManager is used to add and remove determiners in front of words.
 */
class DeterminerManager {

    private val determiners = listOf("a", "an", "the")
    private val anPatterns = jacksonObjectMapper().readValue<List<String>>(object{}.javaClass.getResourceAsStream("/anPatterns.json"))

    /**
     * Takes a string and removes any determiner at the very start of it. It will not remove non-determiners, or
     * determiners in the middle of the string.
     *
     * @param string The string representation of the word
     */
    fun removeDeterminer(string: String): String {
        return determiners.fold(string) {str, det -> Regex("^$det ").replace(str, "")}
    }

    /**
     * Finds the default determiner that should be applied to a particular word. Typically this means an "a" for a word
     * with a consonant sound, and an "an" for a vowel sound.
     *
     * @param string The string representation of the word
     */
    fun chooseDefaultDeterminer(string: String): String {
        val start = string.subSequence(0, 3)
        val cleaned = start.replace(Regex("[^a-zA-Z]"), "*")
        return when(cleaned in anPatterns) {
            true -> "an"
            false -> "a"
        }
    }
}