package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.JokeSpec

const val PRIMARY_PLACEHOLDER = "$1"
const val SECONDARY_PLACEHOLDER = "$2"
const val PUN_PLACEHOLDER = "$3"
const val NO_DETERMINER = "_noDet"

/**
 * A template can be used to generate part of a joke. The template is valid if each and every constraint can apply to
 * the supplied joke spec.
 */
abstract class Template(val id: String,
                        val constraints: List<TemplateConstraint>,
                        val script: String) {
    /**
     * Checks whether or not the template can be used with the current joke spec. This means that all of the
     * template's conditions will be checked to see if they are valid.
     *
     * @param spec The JokeSpec to validate against this template
     */
    fun isValid(spec: JokeSpec):Boolean{
        return constraints.all{it.applies(spec)}
    }
}

/**
 * A setup template is a template that can be used to generate the setup for a joke. The template is valid if each
 * and every constraint can apply to the supplied joke spec.
 *
 * The output is determined by the setupScript, which is a string containing slots for the joke placeholders. These are
 * indicated using the PRIMARY_PLACEHOLDER and SECONDARY_PLACEHOLDER values. To use the version of either without a
 * determiner, add the NO_DETERMINER value to the placeholder. I.e. "What type of $1_noDet has $2?"
 *
 * @property id             The unique id to refer back to this template. Used for traceability of a finished joke.
 * @param script            The script that determines the output joke setup. It's a string containing placeholders
 *                          for the semantic substitutions.
 */
class SetupTemplate(id: String,
                    constraints: List<TemplateConstraint>,
                    script: String) : Template(id, constraints, script){

    /**
     * Uses this template to generate a setup for the supplied substitutions.
     *
     * @param primary The string (including a determiner) which fits the primary setup slot
     * @param secondary The string (including a determiner) which fits the secondary setup slot
     */
    fun apply(primary: String, secondary: String, determinerManager: DeterminerManager):String{
        return script.replace(PRIMARY_PLACEHOLDER + NO_DETERMINER, determinerManager.removeDeterminer(primary))
                     .replace(SECONDARY_PLACEHOLDER + NO_DETERMINER, determinerManager.removeDeterminer(secondary))
                     .replace(PRIMARY_PLACEHOLDER, primary)
                     .replace(SECONDARY_PLACEHOLDER, secondary)
                     .capitalize()
    }
}

/**
 * A setup template is a template that can be used to generate the setup for a joke. The template is valid if each
 * and every constraint can apply to the supplied joke spec.
 *
 * The output is determined by the script, which is a string containing a slot for the pun placeholders. This is
 * indicated using the PUN_PLACEHOLDER value. To use the version without a determiner, add the NO_DETERMINER value
 * to the placeholder. I.e. "$1_noDet has $2?"
 *
 * @property id             The unique id to refer back to this template. Used for traceability of a finished joke.
 * @param script            The script that determines the output joke setup. It's a string containing placeholders
 *                          for the semantic substitutions.
 */
class PunchlineTemplate(id: String,
                        constraints: List<TemplateConstraint>,
                        script: String) : Template(id, constraints, script){

    /**
     * Uses this template to generate a punchline for the supplied substitutions.
     *
     * @param pun The string (including a determiner) which fits the pun slot
     */
    fun apply(pun: String, determinerManager: DeterminerManager):String{
        return script.replace(PUN_PLACEHOLDER + NO_DETERMINER, determinerManager.removeDeterminer(pun))
                     .replace(PUN_PLACEHOLDER, pun)
                     .capitalize()
    }
}