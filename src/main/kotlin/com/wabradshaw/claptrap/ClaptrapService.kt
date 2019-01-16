package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.generation.JokeGenerator
import com.wabradshaw.claptrap.structure.Joke

/**
 * The main service used by the system to generate jokes
 */
class ClaptrapService(private val jokeDesigner: JokeDesigner,
                      private val jokeGenerator: JokeGenerator = JokeGenerator()) {

    fun tellJoke(topic: String): Joke {
        var spec = jokeDesigner.designJokeFromNucleus(topic)
        return jokeGenerator.generateJoke(spec);
    }

    fun tellJoke(): Joke {
        var spec = jokeDesigner.designRandomJoke()
        return jokeGenerator.generateJoke(spec);
    }
}