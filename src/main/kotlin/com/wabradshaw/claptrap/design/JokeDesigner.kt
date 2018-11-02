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
class JokeDesigner(private val dictionaryRepo: DictionaryRepository,
                   private val semanticRepo: SemanticRepository,
                   private val linguisticRepo: LinguisticRepository,
                   private val validPrimaryRelationships: List<Relationship> = listOf(Relationship.INCLUDES),
                   private val validSecondaryRelationships: List<Relationship> = listOf(Relationship.HAS_A),
                   private val validSimilarities: List<LinguisticSimilarity> = listOf(LinguisticSimilarity.RHYME, LinguisticSimilarity.CONSONANT_MATCH),
                   private val substringGenerator: SubstringGenerator = SubstringGenerator(),
                   private val randomiseSubstringChoice: Boolean = true,
                   private val randomiseSemanticSubstitutions: Boolean = true,
                   private val randomiseLinguisticSubstitutions: Boolean = true,
                   private val requiredFrequency: Double = 0.5){

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
            else -> chooseSetup(nucleusWord, listOf(nucleusWord), validPrimaryRelationships)
        }

        val substrings = substringGenerator.getSubstrings(nucleus).toMutableList()
        if (randomiseSubstringChoice) substrings.shuffle()

        for(substring in substrings) {
            val candidateSubstring = dictionaryRepo.getWord(substring)
            if(candidateSubstring != null && substringMatchesPhonetically(nucleusWord?.pronunciation, candidateSubstring.pronunciation)){
                val linguisticSubs = linguisticRepo
                        .getLinguisticSubs(candidateSubstring, validSimilarities)
                        .filterNot{(c) -> usedWord(c, listOf(nucleusWord, primarySetup?.substitution)) }
                        .filter{(c) -> commonWord(c)}
                        .toMutableList()

                if (randomiseLinguisticSubstitutions) linguisticSubs.shuffle()

                for(linguisticSub in linguisticSubs){
                    val usedWords = listOf(nucleusWord, primarySetup?.substitution, candidateSubstring, linguisticSub.substitution)
                    val secondarySetup = chooseSetup(linguisticSub.substitution, usedWords, validSecondaryRelationships)


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
    private fun chooseSetup(nucleusWord: Word, usedWords: List<Word?>, validRelationships: List<Relationship>): SemanticSubstitution? {
        val options =
                semanticRepo.getSemanticSubs(nucleusWord, validRelationships)
                        .filterNot{(c) -> usedWord(c, usedWords) }
                        .filter{(c) -> commonWord(c)}

        return when (randomiseSemanticSubstitutions){
            true -> options.shuffled().getOrNull(0)
            false -> options.getOrNull(0)
        }
    }


    private fun substringMatchesPhonetically(nucleus : String?, candidate: String): Boolean{
        return true || nucleus == null || nucleus.startsWith(candidate) || nucleus.endsWith(candidate)
    }

    private fun commonWord(candidate: Word) = candidate.frequency > requiredFrequency

    private fun usedWord(candidate: Word, usedWords: List<Word?>) =
            candidate.spelling in usedWords.map { word -> word?.spelling }
}