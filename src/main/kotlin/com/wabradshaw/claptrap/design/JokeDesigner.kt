package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.*

/**
 * The JokeDesigner is used to design the specification for jokes. It's method generate JokeSpecs
 * that can be turned into actual written jokes.
 */
class JokeDesigner(val dictionaryRepo: DictionaryRepository,
                   val semanticRepo: SemanticRepository,
                   val linguisticRepo: LinguisticRepository,
                   val validPrimaryRelationships: List<Relationship> = listOf(Relationship.INCLUDES, Relationship.SYNONYM),
                   val validSimilarities: List<LinguisticSimilarity> = listOf(LinguisticSimilarity.RHYME),
                   val substringGenerator: SubstringGenerator = SubstringGenerator(),
                   val randomiseSubstringChoice: Boolean = true,
                   val randomiseSemanticSubstitutions: Boolean = true,
                   val randomiseLinguisticSubstitutions: Boolean = true){

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
            else -> chooseSetup(nucleusWord, listOf(nucleusWord))
        }

        val substrings = substringGenerator.getSubstrings(nucleus).toMutableList()
        if (randomiseSubstringChoice) substrings.shuffle()

        for(substring in substrings) {
            val candidateSubstring = dictionaryRepo.getWord(substring)
            if(candidateSubstring != null && substringMatchesPhonetically(nucleusWord?.pronunciation, candidateSubstring.pronunciation)){
                val linguisticSubs = linguisticRepo
                        .getSubstitutions(candidateSubstring, validSimilarities)
                        .filterNot{(c) -> usedWord(c, listOf(nucleusWord, primarySetup?.substitution)) }
                        .toMutableList()

                if (randomiseLinguisticSubstitutions) linguisticSubs.shuffle()

                for(linguisticSub in linguisticSubs){
                    val secondarySetup = chooseSetup(linguisticSub.substitution, listOf(nucleusWord, primarySetup?.substitution, candidateSubstring, linguisticSub.substitution))

                    if(secondarySetup != null){
                        return JokeSpec(nucleus, nucleusWord, primarySetup, secondarySetup, linguisticSub)
                    }
                }
            }
        }
        throw NoJokeException("No joke could be found for the word '$nucleus'.")
    }

    /**
     * Chooses the word that primes the reader for the punchline. If there are no known semantic
     * substitutions, this will return null.
     */
    private fun chooseSetup(nucleusWord: Word, usedWords: List<Word?>): SemanticSubstitution? {
        var options =
                semanticRepo.getSubstitutions(nucleusWord, validPrimaryRelationships)
                        .filterNot{(c) -> usedWord(c, usedWords) }

        return when (randomiseSemanticSubstitutions){
            true -> options.shuffled().getOrNull(0)
            false -> options.getOrNull(0)
        }
    }


    private fun substringMatchesPhonetically(nucleus : String?, candidate: String): Boolean{
        return nucleus == null || nucleus.startsWith(candidate) || nucleus.endsWith(candidate)
    }

    private fun usedWord(candidate: Word, usedWords: List<Word?>) =
            candidate.spelling in usedWords.map { word -> word?.spelling }
}