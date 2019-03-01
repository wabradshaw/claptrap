package com.wabradshaw.claptrap.design

import java.lang.Math.min

/**
 * A SubstringGenerator is used to find substrings of words that are candidates for substitution. These will later be
 * reduced to just the substrings representing actual words. The default setting is to select substrings at the start
 * and end of the word with between 2 and 5 characters (inclusive).
 *
 * @property maxLength The maximum number of characters in a substring. Inclusive.
 * @property minLength The minimum number of characters in a substring. Inclusive.
 */
class SubstringGenerator(val maxLength: Int = 5,
                         val minLength: Int = 2){

    /**
     * Gets all substrings in a word (or series of words) which are suitable for substitution. The results are in order
     * of length, longest to shortest. Ties are given to the substring from the start of the original word. For example:
     * getSubstrings("abcdefgh") -> "abcde", "defgh", "abcd", "efgh", "abc", "fgh"
     *
     * Duplicate words are ignored, e.g. in "tuktuk", the word "tuk" would only appear once as a substring.
     *
     * In situations where the input contains a series of words each word is considered separately. Ordering is the
     * same as for a single word: by length, with start before end.
     * E.g. "washing machine" would start "washe", "machi", "shing", "chine", "wash", "mach", "hing" ...
     *
     * Note that this only gathers the substrings, it doesn't ensure that they are real words, or phonetically similar
     * to the original word.
     *
     * @param original The word (or series of words) to find substrings from.
     * @return A list of substrings at either the start or end of the word that could be potential substitutions.
     */
    fun getSubstrings(original: String): List<String> {
        val resultSet = LinkedHashSet<String>()

        val words = original.split(" ")

        for (i in maxLength downTo minLength) {
            val options = words.filter{w -> w.length >= i}
            options.forEach{w -> resultSet.add(w.substring(0, i))}
            options.forEach{w -> resultSet.add(w.substring(w.length - i))}
        }

        return resultSet.toList()
    }
}