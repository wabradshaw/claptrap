package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.Joke
import com.wabradshaw.claptrap.structure.JokeSpec
import com.wabradshaw.claptrap.structure.Relationship

class JokeGenerator {

    private val substituter = JokeSubstituter()

    fun generateJoke(spec: JokeSpec): Joke {

        val primarySetup = spec.primarySetup?.substitution?.spelling ?: spec.nucleus
        val secondarySetup = spec.secondarySetup.substitution.spelling

        val joke = substituter.createJokeWord(spec)

        val conjunction = when (spec.secondarySetup.relationship) {
            Relationship.HAS_A -> "with"
            Relationship.IN -> "in"
            Relationship.ON -> "on"
            Relationship.FROM -> "from"
            Relationship.NEAR_SYNONYM -> "a bit like"
            else -> "mixed with"
        }
        return Joke("What do you call a $primarySetup $conjunction $secondarySetup?",
                    "A $joke!",
                    spec)
    }
}