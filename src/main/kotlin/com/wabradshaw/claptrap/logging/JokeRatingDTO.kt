package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.JokeDTO

/**
 * A data transfer object representing a user's rating for a joke.
 */
class JokeRatingDTO {

    // How good they thought the joke was. Positive if they liked it, negative if they didn't.
    var vote: Int = 0

    // The joke in question.
    lateinit var joke: JokeDTO
}