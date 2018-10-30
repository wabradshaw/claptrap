package com.wabradshaw.claptrap.repositories

import com.wabradshaw.claptrap.structure.Relationship
import com.wabradshaw.claptrap.structure.Word
import com.wabradshaw.claptrap.structure.SemanticSubstitution

/**
 * A SemanticRepository is an interface for connectors to different dictionaries that contain relationships
 * between words. That is, words which can be substituted for an original according to a particular relationship.
 */
interface SemanticRepository {

    /**
     * Gets a list of all of the words in the dictionary that can be substituted for the supplied original word,
     * provided they are related by one of the supplied relationships. If no such words exist in the dictionary, then
     * an empty list is returned.
     *
     * @param word The original word to be substituted.
     * @param validRelationships A list of all of the Relationships that constitute valid substitutions.
     * @return A list of the possible SemanticSubstitutions for the original word.
     */
    fun getSemanticSubs(word: Word, validRelationships: List<Relationship>): List<SemanticSubstitution>

}