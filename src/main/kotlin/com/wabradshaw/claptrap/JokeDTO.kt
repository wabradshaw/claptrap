package com.wabradshaw.claptrap

class JokeDTO {
    lateinit var setup: String
    lateinit var punchline: String

    lateinit var template: String
    lateinit var nucleus: String

    var primarySetup: String? = null
    var primaryRelationship: String? = null

    lateinit var secondarySetup: String
    lateinit var secondaryRelationship: String

    lateinit var linguisticOriginal: String
    lateinit var linguisticReplacement: String
}