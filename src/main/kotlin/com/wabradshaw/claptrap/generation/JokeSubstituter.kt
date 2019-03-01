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
     * The substituter will attempt to match the capitalisation of the original string as much as possible. If the
     * replaced segment of the original string would be capitalised (init-caps or all caps), the substitution will be
     * likewise. If the substitution is already capitalised,
     *
     * E.g.
     * linguistically substituting "lot" for "yacht"
     * lottery -> yacht-tery
     * ballot -> bal-yacht
     * allotted -> al-yacht-ed
     * lotus lottery -> yacht-us lottery
     * slot lottery -> slot yacht-tery
     * Lottery -> Yacht-tery
     * LOTTery -> YACHT-TERY
     * @property spec The specification for the joke
     */
    fun createJokeWord(spec: JokeSpec): String{
        val nucleus = spec.nucleus
        val replaced = spec.linguisticSub.original.spelling
        val substitution = spec.linguisticSub.substitution.spelling

        val jokeWord = substitute(nucleus, replaced, substitution)

        // Remove potential artifacts
        return jokeWord.replace("--","-")
                       .replace(" -"," ")
                       .replace("- "," ")
                       .replace("-(?=\$)".toRegex(),"")
    }

    private fun substitute(nucleus: String, replaced: String, substitution: String): String {
        var jokeWord = smartReplace(nucleus, "(?<=(^| ))$replaced", "$substitution-", substitution)
        if(jokeWord == nucleus) {
            jokeWord = smartReplace(nucleus,"$replaced(?=(\$| ))", "-$substitution", substitution)
        }
        if(jokeWord == nucleus) {
            jokeWord = smartReplace(nucleus, replaced, "-$substitution-", substitution)
        }

        return jokeWord
    }

    private fun smartReplace(base: String, pattern: String, substitutionPatter: String, substitute: String): String {
        val match = pattern.toRegex(RegexOption.IGNORE_CASE).find(base)
        return if(match != null){
            val casedSubstitute = when {
                match.value.all { c -> c.isUpperCase() } -> substitute.toUpperCase()
                match.value.first().isUpperCase() -> substitute.capitalize()
                else -> substitute
            }
            val casedSubPattern = substitutionPatter.replace(substitute, casedSubstitute)
            base.substring(0, match.range.first) + casedSubPattern + base.substring(match.range.last+1)
        } else {
            base
        }
    }
}