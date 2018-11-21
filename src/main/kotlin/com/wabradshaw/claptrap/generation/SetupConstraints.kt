package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.JokeSpec
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Relationship

/**
 * A setup constraint type defines a type of constraint that can be put on the joke spec. For example, SECONDARY_POS
 * is a constraint on the part of speech for the secondary setup word.
 *
 * @property function The function that checks whether the constraint is true for the given joke spec and argument
 */
enum class SetupConstraintType(val function: (spec: JokeSpec, arg: String) -> Boolean) {

    /* Constrains whether or not the primary setup exists as a known word. */
    PRIMARY_KNOWN({spec, known -> known.toBoolean() == (spec.primarySetup != null)}),

    /* Constrains whether or not the nucleus exists as a known word. */
    NUCLEUS_KNOWN({spec, known -> known.toBoolean() == (spec.nucleusWord != null)}),

    /* Constrains what the part of speech the primary setup word must have. Fails if unknown. */
    PRIMARY_POS({spec, pos -> PartOfSpeech.valueOf(pos) == spec.primarySetup?.substitution?.partOfSpeech}),

    /* Constrains what the part of speech the secondary setup word must have. */
    SECONDARY_POS({spec, pos -> PartOfSpeech.valueOf(pos) == spec.secondarySetup.substitution.partOfSpeech}),

    /* Constrains what the part of speech the nucleus word must have. Fails if unknown. */
    NUCLEUS_POS({spec, pos -> PartOfSpeech.valueOf(pos) == spec.nucleusWord?.partOfSpeech}),

    /* Constrains what the relationship is between the primary setup word and the nucleus. Fails if unknown. */
    PRIMARY_RELATIONSHIP({spec, rel -> Relationship.valueOf(rel) == spec.primarySetup?.relationship}),

    /* Constrains what the relationship is between the secondary setup word and the nucleus substitute. */
    SECONDARY_RELATIONSHIP({spec, rel -> Relationship.valueOf(rel) == spec.secondarySetup.relationship})
}

/**
 * A SetupConstraint defines a constraint that must be true for a SetupTemplate to be used.
 */
class SetupConstraint(val constraint: SetupConstraintType, val arg: String){

    /**
     * Checks whether or not this particular constraint applies to the given joke.
     *
     * @param spec The JokeSpec to evaluate
     */
    fun applies(spec: JokeSpec): Boolean{
        return constraint.function.invoke(spec, arg)
    }
}