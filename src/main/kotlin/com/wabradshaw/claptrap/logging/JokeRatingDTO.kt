package com.wabradshaw.claptrap.logging

class JokeRatingDTO {
    lateinit var setup: String
    lateinit var punchline: String
    var vote: Int = 0
    lateinit var template: String
    lateinit var nucleus: String
    var primarySetup: String? = null
    lateinit var secondarySetup: String
    lateinit var linguisticOriginal: String
    lateinit var linguisticReplacement: String
    var primaryRelationship: String? = null
    lateinit var secondaryRelationship: String
}