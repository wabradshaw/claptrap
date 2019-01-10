package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.generation.json.SetupTemplateLoader
import com.wabradshaw.claptrap.structure.Joke
import com.wabradshaw.claptrap.structure.JokeSpec

class JokeGenerator(private val setupTemplates: List<SetupTemplate> = SetupTemplateLoader().load(),
                    private val substituter:JokeSubstituter = JokeSubstituter(),
                    private val determinerManager:DeterminerManager = DeterminerManager()) {

    fun generateJoke(spec: JokeSpec): Joke {

        val primarySetup = spec.primarySetup?.substitution?.spelling ?: spec.nucleus
        val secondarySetup = spec.secondarySetup.substitution.spelling

        val setup = chooseSetup(spec, primarySetup, secondarySetup)

        return Joke(setup.apply(primarySetup, secondarySetup, determinerManager),
                    createPunchline(spec),
                    spec,
                    setup.id)
    }

    private fun chooseSetup(spec: JokeSpec, primarySetup: String, secondarySetup: String): SetupTemplate {
        val validTemplates = setupTemplates.filter { it.isValid(spec) }

        if (validTemplates.isEmpty()) {
            throw NoJokeException("Could not work out how to write a joke setup for $primarySetup/$secondarySetup.")
        }

        return validTemplates.shuffled()[0]
    }

    private fun createPunchline(spec: JokeSpec): String {
        val joke = substituter.createJokeWord(spec)
        val determiner = determinerManager.chooseDefaultDeterminer(joke)
        return "$determiner $joke!".capitalize()
    }

}