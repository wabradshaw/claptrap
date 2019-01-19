package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.generation.JokeGenerator
import com.wabradshaw.claptrap.structure.*

/**
 * The main service used by the system to generate jokes
 */
class ClaptrapService(private val jokeDesigner: JokeDesigner,
                      private val jokeGenerator: JokeGenerator = JokeGenerator()) {

    fun tellJoke(topic: String): Joke {
        var spec = jokeDesigner.designJokeFromNucleus(topic)
        return jokeGenerator.generateJoke(spec)
    }

    fun tellJoke(): Joke {
        var spec = jokeDesigner.designRandomJoke()
        return jokeGenerator.generateJoke(spec)
    }

    fun regenerateJoke(
            nucleus: String,
            linguisticOriginal: String,
            linguisticSubstitute: String,
            primarySetup: String,
            primaryRelationship: Relationship,
            secondarySetup: String,
            secondaryRelationship: Relationship
    ): Joke {
        var spec = JokeSpec(nucleus = nucleus,
                            nucleusWord = asWord(nucleus),
                            linguisticSub = LinguisticSubstitution(substitution = asWord(linguisticSubstitute),
                                                                   original = asWord(linguisticOriginal),
                                                                   similarity = LinguisticSimilarity.RHYME),
                            primarySetup = SemanticSubstitution(substitution = asWord(primarySetup),
                                                                original = asWord(nucleus),
                                                                relationship = primaryRelationship),
                            secondarySetup = SemanticSubstitution(substitution = asWord(secondarySetup),
                                                                  original = asWord(linguisticSubstitute),
                                                                  relationship = secondaryRelationship))
        return jokeGenerator.generateJoke(spec)
    }

    /**
     * Converts a string into a placeholder word object.
     */
    private fun asWord(string: String): Word {
        return Word(string, string, PartOfSpeech.UNKNOWN, 100.00)
    }
}