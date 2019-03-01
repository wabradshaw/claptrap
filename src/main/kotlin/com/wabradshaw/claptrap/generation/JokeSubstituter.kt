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
     * In situations where there are multiple words, the ordering still goes start > end > middle. As such, the first
     * start that matches will be replaced. If none are found then the first end will be used, otherwise the first
     * middle.
     *
     * E.g.
     * linguistically substituting "lot" for "yacht"
     * lottery -> yacht-tery
     * ballot -> bal-yacht
     * allotted -> al-yacht-ed
     * lotus lottery -> yacht-us lottery
     * slot lottery -> slot yacht-tery
     */
    fun createJokeWord(spec: JokeSpec): String{
        val nucleus = spec.nucleus
        val replaced = spec.linguisticSub.original.spelling
        val substitution = spec.linguisticSub.substitution.spelling

        val jokeWord = replace(nucleus, replaced, substitution)

        // Remove potential artifacts
        return jokeWord.replace("--","-")
                       .replace(" -"," ")
                       .replace("- "," ")
                       .replace("-(?=\$)".toRegex(),"")
    }

    private fun replace(nucleus: String, replaced: String, substitution: String): String {
        var replacement = nucleus.replaceFirst("(?<=(^| ))$replaced".toRegex(RegexOption.IGNORE_CASE), "$substitution-")
        if(replacement == nucleus) {
            replacement = nucleus.replaceFirst("$replaced(?=(\$| ))".toRegex(RegexOption.IGNORE_CASE), "-$substitution")
        }
        if(replacement == nucleus) {
            replacement = nucleus.replaceFirst(replaced, "-$substitution-", ignoreCase = true)
        }

        return replacement
    }
}