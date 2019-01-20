package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.JokeDTO

/**
 * A data transfer object representing a user's suggestion for a better joke.
 */
class JokeImprovementDTO {

    // The joker produced by Claptrap
    lateinit var oldJoke: JokeDTO

    // The joke produced by the user
    lateinit var newJoke: JokeDTO
}