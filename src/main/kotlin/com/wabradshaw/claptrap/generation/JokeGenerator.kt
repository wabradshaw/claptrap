package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.generation.json.PunchlineTemplateLoader
import com.wabradshaw.claptrap.generation.json.SetupTemplateLoader
import com.wabradshaw.claptrap.structure.Joke
import com.wabradshaw.claptrap.structure.JokeSpec

/**
 * A JokeGenerator takes the specification for a joke and fleshes it out with text. It maintains a store of templates
 * that can be used for joke setups and punchlines.
 *
 * @property setupTemplates The list of templates that can be used to produce setups. Defaults to the standard list.
 * @property punchlineTemplates The list of templates that can be used to produce punchlines. Defaults to the standard
 *                              list.
 * @property substituter The JokeSubstituter used to create the actual pun word itself. Defaults to a standard one.
 * @property determinerManager The DeterminerManager used to add and remove determiners. Defaults to a standard one.
 */
class JokeGenerator(private val setupTemplates: List<SetupTemplate> = SetupTemplateLoader().load(),
                    private val punchlineTemplates: List<PunchlineTemplate> = PunchlineTemplateLoader().load(),
                    private val substituter:JokeSubstituter = JokeSubstituter(),
                    private val determinerManager:DeterminerManager = DeterminerManager()) {

    /**
     * Generates the text of the joke described by the provided joke spec. The exact form of the joke will be
     * randomly selected from the list of possible joke templates that could apply to the individual spec.
     *
     * Throws an exception if no templates can apply in this particular spec's case.
     *
     * @param spec The spec defining the joke
     */
    fun generateJoke(spec: JokeSpec): Joke {

        val primarySetup = spec.primarySetup?.substitution?.spelling ?: spec.nucleus
        val secondarySetup = spec.secondarySetup.substitution.spelling

        val jokeWord = substituter.createJokeWord(spec)
        val determiner = determinerManager.chooseDefaultDeterminer(jokeWord)

        val setup = choose(setupTemplates, spec)
        val punchline = choose(punchlineTemplates, spec)

        return Joke(setup.apply(primarySetup, secondarySetup, determinerManager),
                    punchline.apply("$determiner $jokeWord", determinerManager),
                    spec,
                    setup.id,
                    punchline.id)
    }

    /**
     * Chooses a random valid template for the given joke spec. Throws an exception if there are no valid templates.
     *
     * @param templates The list of all possible templates
     * @param spec The specification for the joke being generated
     */
    private fun <T: Template>choose(templates: List<T>, spec: JokeSpec): T {
        val validTemplates = templates.filter { it.isValid(spec) }

        if (validTemplates.isEmpty()) {
            throw NoJokeException("Could not work out how to write a joke setup for $spec.")
        }

        return validTemplates.shuffled()[0]
    }

}