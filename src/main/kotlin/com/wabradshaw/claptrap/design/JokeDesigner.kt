package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.*

/**
 * The JokeDesigner is used to design the specification for jokes. It's method generate JokeSpecs
 * that can be turned into actual written jokes.
 */
class JokeDesigner(val dictionaryRepo: DictionaryRepository,
                   val semanticRepo: SemanticRepository,
                   val validPrimaryRelationships: List<Relationship> = listOf(Relationship.INCLUDES, Relationship.SYNONYM),
                   val randomiseSemanticSubstitutions: Boolean = true){

    /**
     * Generates the joke specification for a joke where the user has supplied the principle word in the
     * joke punchline. This nucleus word will be altered to create a funnier alternative.
     *
     * Please note that this is a joke telling AI. The joke will probably be rubbish.
     *
     * @param nucleus The word that should be the focus for the joke. It will appear in the body.
     */
    fun designJokeFromNucleus(nucleus: String): JokeSpec {

        val nucleusWord = dictionaryRepo.getWord(nucleus)

        val primarySetup: SemanticSubstitution? = when (nucleusWord) {
            null -> null
            else -> choosePrimarySetup(nucleusWord)
        }

        return TODO()
    }

    /**
     * Chooses the word that primes the reader for the main word in the punchline. If there are no known semantic
     * substitutions, this will return null.
     */
    private fun choosePrimarySetup(nucleusWord: Word): SemanticSubstitution? {
        var options = semanticRepo.getSubstitutions(nucleusWord, validPrimaryRelationships)
        return when (randomiseSemanticSubstitutions){
            true -> options.shuffled().getOrNull(0)
            false -> options.getOrNull(0)
        }
    }
}