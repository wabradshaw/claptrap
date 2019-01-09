package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.JokeSpec

/**
 * A JokeSubstituter is a class designed to produce a finished joke by substituting the semantic substitution into
 * the nucleus word.
 */
class JokeSubstituter {

    /**
     * Takes the specification for a joke and creates the actual joke word in the punchline. This will be created by
     * applying the linguistic substitution to the nucleus. Where possible, the start of the word will be replaced.
     * Otherwise it will be the end of the joke. If the substitution isn't at one of the ends, then and only then will
     * a substitution in the middle be used. A substitution will have hyphens between it and the rest of the nucleus.
     *
     * E.g.
     * linguistically substituting "lot" for "yacht"
     * lottery -> yacht-tery
     * ballot -> bal-yacht
     * allotted -> al-yacht-ed
     */
    fun createJokeWord(spec: JokeSpec): String{
        val nucleus = spec.nucleus
        val replaced = spec.linguisticSub.original.spelling
        val substitution = spec.linguisticSub.substitution.spelling

        val jokeWord = when {
            nucleus.startsWith(replaced, ignoreCase = true) -> nucleus.replaceFirst(replaced, substitution + "-", ignoreCase = true)
            nucleus.endsWith(replaced, ignoreCase = true) -> nucleus.substring(0, spec.nucleus.length - replaced.length) + "-" + substitution
            else -> nucleus.replaceFirst(replaced, "-$substitution-", ignoreCase = true)
        }

        // Remove potential artifacts
        return jokeWord.replace("--","-")
                       .replace(" -"," ")
                       .replace("- "," ")
    }
}