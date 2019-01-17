package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.JokeDTO

/**
 * A data transfer object representing a user's rating for a relationship.
 */
class RelationRatingDTO {

    // Whether or not they thought the relationship was wrong
    var wrong: Boolean = false

    // The relationship in question.
    lateinit var relationship: RelationDTO
}