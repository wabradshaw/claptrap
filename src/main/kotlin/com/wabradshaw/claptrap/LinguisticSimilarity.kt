package com.wabradshaw.claptrap

/**
 * A linguistic similarity links an original word and a different linguistic substitution word.
 */
enum class LinguisticSimilarity {

    /**
     * The two words rhyme. E.g. horse RHYMEs with course
     */
    RHYME,

    /**
     * The two words sort of rhyme. The type of rhyme that's a bit disappointing. E.g. horse NEARly_RHYMEs with curse.
     */
    NEAR_RHYME,

    /**
     * The two words sound alike. E.g. horse is a HOMOPHONE of hoarse.
     */
    HOMOPHONE,

    /**
     * The two words have the same consonants, but different vowels. E.g. horse CONSONANT_MATCHes hearse.
     * (In this case the two words are also near rhymes)
     */
    CONSONANT_MATCH
}