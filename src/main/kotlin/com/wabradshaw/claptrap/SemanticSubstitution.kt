package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.PartOfSpeech
import com.wabradshaw.claptrap.Relationship
import com.wabradshaw.claptrap.Word

/**
 * A semantic substitution is a word that can be used to replace a particular original word with another word that is
 * somehow semantically linked to the original word. This semantic link is defined by a given relationship.
 * E.g. "animal" could be a substitute for "cat" with an IS_A relationship.
 *
 * Semantic substitutions are used when creating the setup for a joke.
 *
 * @property substitution The word that can be used as a substitute for the original word.
 * @property original The word to be replaced.
 * @property relationship The Relationship describing how the original relates to the substitution. E.g. if the original
 *                        word was "cat" and the substitute was "animal", it would be an IS_A relationship.
 */
data class SemanticSubstitution(val substitution: Word,
                                val original: Word,
                                val relationship: Relationship)