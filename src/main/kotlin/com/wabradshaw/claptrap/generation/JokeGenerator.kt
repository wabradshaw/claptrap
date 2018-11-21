package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.generation.json.SetupTemplateLoader
import com.wabradshaw.claptrap.structure.Joke
import com.wabradshaw.claptrap.structure.JokeSpec

class JokeGenerator(private val setupTemplates: List<SetupTemplate> = SetupTemplateLoader().load(),
                    private val substituter:JokeSubstituter = JokeSubstituter()) {

    fun generateJoke(spec: JokeSpec): Joke {

        val primarySetup = spec.primarySetup?.substitution?.spelling ?: spec.nucleus
        val secondarySetup = spec.secondarySetup.substitution.spelling

        val joke = substituter.createJokeWord(spec)

        val validTemplates = setupTemplates.filter{it.isValid(spec)}

        if(validTemplates.isEmpty()){
            throw NoJokeException("Could not work out how to write a joke setup for $primarySetup/$secondarySetup.")
        }

        val setup = validTemplates.shuffled()[0]

        return Joke(setup.apply(primarySetup, secondarySetup),
                    "A $joke!",
                    spec,
                    setup.id)
    }
}