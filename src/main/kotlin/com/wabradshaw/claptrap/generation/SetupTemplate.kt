package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.JokeSpec

const val PRIMARY_PLACEHOLDER = "$1"
const val SECONDARY_PLACEHOLDER = "$2"
const val NO_DETERMINER = "_noDet"

/**
 * A setup template is a template that can be used to generate the setup for a joke. Each setup has certain restrictions
 * about when it can be used.
 *
 * The output is determined by the setupScript, which is a string containing slots for the joke placeholders. These are
 * indicated using the PRIMARY_PLACEHOLDER and SECONDARY_PLACEHOLDER values. To use the version of either without a
 * determiner, add the NO_DETERMINER value to the placeholder. I.e. "What type of $1_noDet has $2?"
 *
 * @property id The unique id to refer back to this template. Used for traceability of a finished joke.
 * @param setupScript The script that determines the output joke setup. It's a string containing placeholders for the
 *                    semantic substitutions.
 * @param determinerManager The determiner manager to use to add or remove determiners. Defaults to the typical one.
 */
class SetupTemplate(val id: Int,
                    private val setupScript: String,
                    private val determinerManager: DeterminerManager = DeterminerManager()) {

    /**
     * Checks whether or not the setup template can be used with the current joke spec. This means that all of the
     * template's conditions will be checked to see if they are valid.
     */
    fun isValid(spec: JokeSpec):Boolean{
        return true
    }

    /**
     * Uses this setup template to generate a template for the supplied substitutions.
     *
     * @param primary The string (including a determiner) which fits the primary setup slot
     * @param secondary The string (including a determiner) which fits the secondary setup slot
     */
    fun apply(primary: String, secondary: String):String{
        return setupScript.replace(PRIMARY_PLACEHOLDER + NO_DETERMINER, determinerManager.removeDeterminer(primary))
                          .replace(SECONDARY_PLACEHOLDER + NO_DETERMINER, determinerManager.removeDeterminer(secondary))
                          .replace(PRIMARY_PLACEHOLDER, primary)
                          .replace(SECONDARY_PLACEHOLDER, secondary)
    }
}