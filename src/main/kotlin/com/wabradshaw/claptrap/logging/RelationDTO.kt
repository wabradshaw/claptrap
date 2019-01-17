package com.wabradshaw.claptrap.logging

class RelationDTO {

    // The primary word in the relationship
    lateinit var original: String

    // The secondary word in the relationship
    lateinit var substitution: String

    // The string form of the relationship between the two words
    lateinit var relationship : String

    // The position of the relationship in the joke
    lateinit var position : String
}