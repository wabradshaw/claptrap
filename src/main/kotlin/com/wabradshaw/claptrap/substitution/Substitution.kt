package com.wabradshaw.claptrap.substitution

import com.wabradshaw.claptrap.PartOfSpeech
import com.wabradshaw.claptrap.Relationship

/**
 * A substitution is a word that can be used to replace a particular original word. The replacement can be used based
 * on a given relationship. E.g. "animal" could be a substitute for "cat" with an IS_A relationship.
 *
 * @property substitution The word can be used as a substitute for the original word.
 * @property original The word to be replaced.
 * @property pos The part of speech of the substitute word. I.e. how to use it in a sentence.
 * @property relationship The Relationship describing how the original relates to the substitution. E.g. if the original
 *                        word was "cat" and the substitute was "animal", it would be an IS_A relationship.
 */
data class Substitution(val substitution: String,
                        val original: String,
                        val pos: PartOfSpeech,
                        val relationship: Relationship)