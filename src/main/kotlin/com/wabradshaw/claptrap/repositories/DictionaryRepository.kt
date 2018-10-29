package com.wabradshaw.claptrap.repositories

import com.wabradshaw.claptrap.structure.Word

/**
 * A DictionaryRepository is an interface for connectors to different dictionaries that data on individual words.
 */
interface DictionaryRepository {

    /**
     * Gets the Word object for a particular spelling of a word from the dictionary.
     *
     * @param word The spelling of the word to lookup.
     * @return The details of the word, or null if the word doesn't exist in the database.
     */
    fun getWord(string: String ): Word?
}