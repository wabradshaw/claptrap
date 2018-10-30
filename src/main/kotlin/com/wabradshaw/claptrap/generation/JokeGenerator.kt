package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.structure.Joke
import com.wabradshaw.claptrap.structure.JokeSpec

class JokeGenerator {

    fun generateJoke(spec: JokeSpec): Joke {

        val primarySetup = spec.primarySetup?.substitution?.spelling ?: spec.nucleus
        val secondarySetup = spec.secondarySetup.substitution.spelling

        val replaced = spec.linguisticSub.original.spelling
        val substitution = spec.linguisticSub.substitution.spelling

        val joke = when (spec.nucleus.startsWith(replaced)){
            true -> spec.nucleus.replaceFirst(replaced, substitution + "-")
            false -> spec.nucleus.substring(0, spec.nucleus.length - replaced.length) + "-" + substitution
        }
        return Joke("What do you call a ${primarySetup} with a ${secondarySetup}?",
                    "A $joke!",
                    spec)
    }
}