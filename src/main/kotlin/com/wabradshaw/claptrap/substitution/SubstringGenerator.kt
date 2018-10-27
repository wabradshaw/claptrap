package com.wabradshaw.claptrap.substitution

import java.lang.Math.min

/**
 * A SubstringGenerator is used to find substrings of words that are candidates for substitution. These will later be
 * reduced to just the substrings representing actual words. The default setting is to select substrings at the start
 * and end of the word with between 3 and 5 characters (inclusive).
 *
 * @property maxLength The maximum number of characters in a substring. Inclusive.
 * @property minLength The minimum number of characters in a substring. Inclusive.
 */
class SubstringGenerator(val maxLength: Int = 5,
                         val minLength: Int = 3){

    /**
     * Gets all substrings in a word which are suitable for substitution. The results are in order of length, longest
     * to shortest. Ties are given to the substring from the start of the original word. For example:
     * getSubstrings("abcdefgh") -> "abcde", "defgh", "abcd", "efgh", "abc", "fgh"
     *
     * Duplicate words are ignored, e.g. in "tuktuk", the word "tuk" would only appear once as a substring.
     *
     * Note that this only gathers the substrings, it doesn't ensure that they are real words, or phonetically similar
     * to the original word.
     *
     * @param original The word to find substrings from.
     * @return A list of substrings at either the start or end of the word that could be potential substitutions.
     */
    fun getSubstrings(original: String): List<String> {

        if (minLength > original.length) {
            return emptyList()
        }

        val resultSet = LinkedHashSet<String>()
        val max = min(original.length, maxLength)

        for (i in max downTo minLength) {
            resultSet.add(original.substring(0, i))
            resultSet.add(original.substring(original.length - i))
        }

        return resultSet.toList()
    }
}