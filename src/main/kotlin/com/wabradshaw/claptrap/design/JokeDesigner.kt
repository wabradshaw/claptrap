package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.repositories.LinguisticRepository
import com.wabradshaw.claptrap.repositories.NucleusRepository
import com.wabradshaw.claptrap.repositories.SemanticRepository
import com.wabradshaw.claptrap.structure.JokeSpec
import com.wabradshaw.claptrap.structure.SemanticSubstitution
import com.wabradshaw.claptrap.structure.Word

/**
 * The JokeDesigner is used to design the specification for jokes. It's method generate JokeSpecs
 * that can be turned into actual written jokes.
 */
class JokeDesigner(private val dictionaryRepo: DictionaryRepository,
                   private val primarySemanticRepo: SemanticRepository,
                   private val secondarySemanticRepo: SemanticRepository,
                   private val linguisticRepo: LinguisticRepository,
                   private val nucleusRepository: NucleusRepository,
                   private val substringGenerator: SubstringGenerator = SubstringGenerator(),
                   private val randomiseSubstringChoice: Boolean = false,
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

        return designJoke(nucleus, nucleusWord)
    }

    /**
     * Generates the joke specification for a joke using a randomly chosen nucleus.
     *
     * Please note that this is a joke telling AI. The joke will probably be rubbish.
     */
    fun designRandomJoke(): JokeSpec {

        val nucleusWord = nucleusRepository.getNucleus()

        return designJoke(nucleusWord.spelling, nucleusWord)
    }

    /**
     * Main workhorse of the JokeDesigner that actually produces the joke.
     */
    private fun designJoke(nucleusString: String, nucleusWord: Word?): JokeSpec {
        val primarySetup: SemanticSubstitution? = when (nucleusWord) {
            null -> null
            else -> chooseSetup(nucleusWord, listOf(nucleusWord), primarySemanticRepo)
        }

        val substrings = substringGenerator.getSubstrings(nucleusString).toMutableList()
        if (randomiseSubstringChoice) substrings.shuffle()

        for (substring in substrings) {
            val candidateSubstring = dictionaryRepo.getWord(substring)
            if (candidateSubstring != null) {
                val linguisticSubs = linguisticRepo
                        .getLinguisticSubs(candidateSubstring)
                        .filterNot { (c) -> usedWord(c, listOf(nucleusWord, primarySetup?.substitution)) }
                        .filter { (c) -> commonWord(c) }
                        .toMutableList()

                if (randomiseLinguisticSubstitutions) linguisticSubs.shuffle()

                for (linguisticSub in linguisticSubs) {
                    val usedWords = listOf(nucleusWord, primarySetup?.substitution, candidateSubstring, linguisticSub.substitution)
                    val secondarySetup = chooseSetup(linguisticSub.substitution, usedWords, secondarySemanticRepo)

                    if (secondarySetup != null) {
                        return JokeSpec(nucleusString, nucleusWord, primarySetup, secondarySetup, linguisticSub)
                    }
                }
            }
        }
        throw NoJokeException("No joke could be found for the word '$nucleusString'.")
    }

    /**
     * Chooses the word that primes the reader for the punchline. If there are no known semantic
     * substitutions, this will return null.
     */
    private fun chooseSetup(nucleusWord: Word, usedWords: List<Word?>, repo: SemanticRepository): SemanticSubstitution? {
        val options =
                repo.getSemanticSubs(nucleusWord)
                        .filterNot{(c) -> usedWord(c, usedWords) }
                        .filter{(c) -> commonWord(c)}

        return when (randomiseSemanticSubstitutions){
            true -> options.shuffled().getOrNull(0)
            false -> options.getOrNull(0)
        }
    }

    private fun commonWord(candidate: Word) = candidate.frequency > requiredFrequency

    private fun usedWord(candidate: Word, usedWords: List<Word?>) =
            candidate.spelling in usedWords.map { word -> word?.spelling }
}