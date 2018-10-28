package com.wabradshaw.claptrap

/**
 * A linguistic substitution is a word that can be used to replace a particular original word with another word that
 * could be linguistically confused for the original word. This linguistic link is defined by a given similarity.
 * E.g. "cart" could be a substitute for "heart" as they rhyme.
 *
 * Linguistic substitutions are used when creating the body of a joke.
 *
 * @property substitution The word that can be used as a linguistic substitute for the original word.
 * @property original     The word to be replaced.
 * @property similarity   The LinguisticSimilarity describing the linguistic link the two words share.
 *                        E.g. if the original word was "cat" and the substitute was "bat", it would be a RHYME.
 */
data class LinguisticSubstitution(val substitution: Word,
                                  val original: Word,
                                  val similarity: LinguisticSimilarity)