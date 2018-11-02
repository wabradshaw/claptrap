package com.wabradshaw.claptrap.repositories

import com.wabradshaw.claptrap.structure.*

/**
 * A LinguisticRepository is an interface for connectors to different dictionaries that contain linguistic links
 * between words. That is, words which can be substituted for an original because they are similar linguistically. For
 * example, the two words rhyme or sound similar.
 */
interface LinguisticRepository {

    /**
     * Gets a list of all of the words in the dictionary that can be substituted for the supplied original word,
     * provided they are similar linguistically. If no such words exist in the dictionary, then an empty list
     * is returned.
     *
     * @param word The original word to be substituted.
     * @return A list of the possible LinguisticSubstitutions for the original word.
     */
    fun getLinguisticSubs(word: Word): List<LinguisticSubstitution>

}