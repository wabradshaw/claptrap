package com.wabradshaw.claptrap.structure

/**
 * A Joke is a bundle containing a generated joke, and the key components that led to this joke's generation.
 *
 * @property setup The string starting the joke off.
 * @property punchline The part of the joke that's meant to be funny.
 * @property spec The specification that was used to create the joke. Primarily used when debugging.
 * @property setupTemplate The template used to generate the joke. Primarily used when debugging.
 */
data class Joke(val setup: String,
                val punchline: String,
                val spec: JokeSpec,
                val setupTemplate: String);