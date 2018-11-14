package com.wabradshaw.claptrap.generation

/**
 * A DeterminerManager is used to add and remove determiners in front of words.
 */
class DeterminerManager {

    private val determiners = listOf("a", "an", "the")

    /**
     * Takes a string and removes any determiner at the very start of it. It will not remove non-determiners, or
     * determiners in the middle of the string.
     */
    fun removeDeterminer(string: String): String {
        return determiners.fold(string) {string, det -> Regex("^$det ").replace(string, "")}
    }
}